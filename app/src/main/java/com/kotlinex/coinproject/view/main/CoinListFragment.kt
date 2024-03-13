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
import com.kotlinex.coinproject.databinding.FragmentCoinListBinding
import com.kotlinex.coinproject.db.entity.InterestCoinEntity
import com.kotlinex.coinproject.view.adapter.CoinListAdapter
import timber.log.Timber

class CoinListFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private var _binding : FragmentCoinListBinding? = null
    private val binding get() = _binding!!

    private val selected = ArrayList<InterestCoinEntity>()
    private val unselected = ArrayList<InterestCoinEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllInterestCoinData()
        viewModel.selectedCoinList.observe(viewLifecycleOwner, Observer {

            selected.clear()
            unselected.clear()

            for (item in it) {
                if(item.selected) {
                    selected.add(item)
                } else {
                    unselected.add(item)
                }
            }

            setSelectedList()

        })
    }

    private fun setSelectedList() {

        val selectedAdapter = CoinListAdapter(requireContext(), selected)
        binding.selectedCoinRV.adapter = selectedAdapter
        binding.selectedCoinRV.layoutManager = LinearLayoutManager(requireContext())

        selectedAdapter.itemClick = object : CoinListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                // like 선택 해제
                viewModel.updateInterestCoinData(selected[position])
            }
        }

        val unSelectedAdapter = CoinListAdapter(requireContext(), unselected)
        binding.unSelectedCoinRV.adapter = unSelectedAdapter
        binding.unSelectedCoinRV.layoutManager = LinearLayoutManager(requireContext())

        unSelectedAdapter.itemClick = object : CoinListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                // like 선택
                viewModel.updateInterestCoinData(unselected[position])
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}