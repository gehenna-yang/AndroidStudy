package com.kotlinex.coinproject.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinex.coinproject.R
import com.kotlinex.coinproject.databinding.FragmentPriceChangeBinding
import com.kotlinex.coinproject.view.adapter.PriceListAdapter

class PriceChangeFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private var _binding : FragmentPriceChangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPriceChangeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllSelectedCoinData()

        viewModel.arr15min.observe(viewLifecycleOwner, Observer {
            val priceListAdapter = PriceListAdapter(requireContext(), it)
            binding.price15m.adapter = priceListAdapter
            binding.price15m.layoutManager = LinearLayoutManager(requireContext())
        })

        viewModel.arr30min.observe(viewLifecycleOwner, Observer {
            val priceListAdapter = PriceListAdapter(requireContext(), it)
            binding.price30m.adapter = priceListAdapter
            binding.price30m.layoutManager = LinearLayoutManager(requireContext())
        })

        viewModel.arr45min.observe(viewLifecycleOwner, Observer {
            val priceListAdapter = PriceListAdapter(requireContext(), it)
            binding.price45m.adapter = priceListAdapter
            binding.price45m.layoutManager = LinearLayoutManager(requireContext())
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}