package com.eiyooooo.foldswitcher.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eiyooooo.foldswitcher.views.HomeFragment
import com.eiyooooo.foldswitcher.views.LogFragment
import com.eiyooooo.foldswitcher.views.ManagerFragment
import com.eiyooooo.foldswitcher.views.SettingsFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> ManagerFragment()
            2 -> LogFragment()
            3 -> SettingsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}