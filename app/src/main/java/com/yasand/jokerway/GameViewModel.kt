package com.yasand.jokerway

import android.content.res.Resources
import androidx.lifecycle.*
import com.yasand.jokerway.data.*
import kotlinx.coroutines.*

typealias Matrix = Array<Array<CellStatus>>

enum class Result {
    WON,
    LOST
}

class GameViewModel(
    private val repository: Repository,
    private val resources: Resources,
    private val player: Player
) : ViewModel() {

    private val PERCENT_FOR_OFFSET = 0.05f
    private val SCALE_PLAYER_SIZE = 3 / 2f
    private val FPS = 60
    private val SPEED = 2f

    private var cellList = arrayListOf<Cell>()

    private var startOffsetBoard = 0
    private var topOffsetBoard = 0
    private var cellSize = 0

    private var levelData: LevelModel? = null
    private var width = 0
    private var height = 0

    private var _boardData = MutableLiveData<LevelModel>()
    val boardData: LiveData<LevelModel>
        get() = _boardData

    private var _gameData = MutableLiveData<Pair<List<Cell>, Player>>()
    val gameData: LiveData<Pair<List<Cell>, Player>>
        get() = _gameData

    private var _gameStatus = MutableLiveData<Result>()
    val gameStatus: LiveData<Result>
        get() = _gameStatus

    private var startPosition = Position(0f, 0f)
    private var directionPlayerX = 0
    private var directionPlayerY = -1

    private var boardBorderLeft = 0
    private var boardBorderRight = 0
    private var boardBorderTop = 0
    private var boardBorderBottom = 0

    private var gameJob: Job? = null

    fun initializeGame(levelData: LevelModel, width: Int, height: Int) {
        this.width = width
        this.height = height
        if (levelData.matrix.isEmpty()) return
        setPlayerState(levelData.startState)
        calculateSize(levelData.matrix.size, levelData.matrix[0].size, width, height)
        initializeBoard(levelData.matrix, resources)
        initializePlayer(levelData.point)
    }

    private fun initializeBoard(data: Matrix, resources: Resources) {
        data.forEachIndexed { row, array ->
            array.forEachIndexed { column, cellStatus ->
                val cell = BoardCell(
                    resources,
                    cellSize,
                    column * cellSize + startOffsetBoard,
                    boardBorderTop + row * cellSize + topOffsetBoard
                ).apply {
                    setCellStatus(cellStatus)
                }
                cellList.add(cell)
            }
        }
    }

    private fun initializePlayer(position: Pair<Int, Int>) {
        startPosition = Position(
            position.first * cellSize + startOffsetBoard + cellSize / 2f,
            boardBorderTop + position.second * cellSize + topOffsetBoard + cellSize / 2f,
        )
        player.setPosition(startPosition)
    }

    private fun calculateSize(countRow: Int, countColumn: Int, width: Int, height: Int) {
        startOffsetBoard = (width * PERCENT_FOR_OFFSET).toInt()
        topOffsetBoard = startOffsetBoard
        cellSize = (width - startOffsetBoard * 2) / countColumn
        boardBorderLeft = startOffsetBoard
        boardBorderRight = startOffsetBoard + cellSize * countColumn
        boardBorderTop = height - topOffsetBoard * 2 - cellSize * countRow
        boardBorderBottom = height - topOffsetBoard
        setPlayerSize()
    }

    private fun setPlayerSize() {
        player.setSize((cellSize * SCALE_PLAYER_SIZE).toInt())
    }

    fun getLevelData() {
        viewModelScope.launch {
            levelData = async { repository.getLevelData() }.await()
            _boardData.value = levelData
        }
    }

    fun setPlayerState(state: PlayerState) {
        when (state) {
            PlayerState.Left -> {
                directionPlayerX = -1
                directionPlayerY = 0
            }
            PlayerState.Right -> {
                directionPlayerX = 1
                directionPlayerY = 0
            }
            PlayerState.Top -> {
                directionPlayerX = 0
                directionPlayerY = -1
            }
            PlayerState.Bottom -> {
                directionPlayerX = 0
                directionPlayerY = 1
            }
        }
        player.setPlayerState(state)
    }

    private fun processUser() {
        player.setOffsetPosition(SPEED * directionPlayerX, SPEED * directionPlayerY)
    }

    private fun checkCollision() {
        val cell = findCollisionCell()
        cell?.let {
            processStatusCell(cell)
        }
        checkPlayerPosition()
    }

    private fun findCollisionCell() =
        cellList.find {
            player.getPlayerCollisionRectangle().intersect(it.getCellCollisionRectangle())
        }

    private fun processStatusCell(cell: Cell) {
        when (cell.getCellStatus()) {
            CellStatus.Inactive, CellStatus.AfterUse, CellStatus.Passed -> {
                sendResult(Result.LOST)
            }
            CellStatus.Use -> {
            }
            CellStatus.Active -> {
                cell.setCellStatus(CellStatus.Use)
                val result = cellList
                    .filter { it != cell }
                    .map {
                        it.changeStatus()
                        it
                    }
                    .findLast { it.getCellStatus() == CellStatus.Active }
                if (result == null) sendResult(Result.WON)
            }
        }
    }

    private fun checkPlayerPosition() {
        if (player.getPlayerCollisionRectangle().centerX() > boardBorderRight ||
            player.getPlayerCollisionRectangle().centerX() < boardBorderLeft
        ) {
            sendResult(Result.LOST)
        }
        if (player.getPlayerCollisionRectangle().centerY() > boardBorderBottom ||
            player.getPlayerCollisionRectangle().centerY() < boardBorderTop
        ) {
            sendResult(Result.LOST)
        }
    }

    private fun sendResult(result: Result) {
        gameJob?.cancel()
        _gameStatus.postValue(result)
        cellList = arrayListOf()
    }

    fun startNewGame() {
        levelData?.let {
            initializeGame(it, width, height)
        }
        startGame()
    }

    private fun createGameJob() {
        gameJob = viewModelScope.launch {
            withContext(Dispatchers.Default) {
                while (isActive) {
                    processUser()
                    checkCollision()
                    _gameData.postValue(Pair(cellList, player))
                    delay((1000 / FPS).toLong())
                }
            }
        }
    }

    fun startGame() {
        if (cellList.isEmpty()) return
        if (gameJob == null) {
            createGameJob()
        } else {
            gameJob?.start()
        }
    }

    fun stopGame() {
        gameJob?.cancel()
        gameJob = null
    }
}