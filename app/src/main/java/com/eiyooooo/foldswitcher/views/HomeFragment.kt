package com.eiyooooo.foldswitcher.views

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.eiyooooo.foldswitcher.adapters.HomeFragmentAdapter
import com.eiyooooo.foldswitcher.databinding.FragmentHomeBinding
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect

class HomeFragment(private val shizukuStatus: LiveData<ShizukuStatus?>) : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val adapter by lazy { HomeFragmentAdapter(shizukuStatus) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.updateData(shizukuStatus.value)

        val recyclerView = binding.list
        recyclerView.adapter = adapter
        recyclerView.fixEdgeEffect()
        recyclerView.addItemSpacing(top = 4f, bottom = 4f, unit = TypedValue.COMPLEX_UNIT_DIP)
        recyclerView.addEdgeSpacing(top = 4f, bottom = 4f, left = 16f, right = 16f, unit = TypedValue.COMPLEX_UNIT_DIP)
    }
}