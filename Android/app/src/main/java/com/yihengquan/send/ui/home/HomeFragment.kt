package com.yihengquan.send.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yihengquan.send.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.setIPAddress(context)

        var binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            // Show keyboard automatically
            if (messageBox.requestFocus()) {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                messageBox.addTextChangedListener {
                   homeViewModel.setMessage(messageBox.text)
                }
            }

            homeViewModel.address.observe(viewLifecycleOwner, Observer {
                myAddressView.text = it
            })
        }

        return binding.root
    }
}