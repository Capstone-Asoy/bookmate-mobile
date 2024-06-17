package com.example.bookmate.ui.explore

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: Fragment, private val tabDataList: List<TabGenreData>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return tabDataList.size
    }

    override fun createFragment(position: Int): Fragment {
        val data = tabDataList[position]
        return GenreFragment.newInstance(data.genre)
    }

}