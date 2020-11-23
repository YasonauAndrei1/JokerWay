package com.yasand.jokerway.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.addFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean = false
) {
    val transaction = beginTransaction()
        .add(containerViewId, fragment, tag)
    if (addToBackStack) {
        transaction.addToBackStack(tag)
    }
    transaction.commit()
}
fun FragmentManager.replaceFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean = false
) {
    val transaction = beginTransaction()
        .replace(containerViewId, fragment, tag)
    if (addToBackStack) {
        transaction.addToBackStack(tag)
    }
    transaction.commit()
}