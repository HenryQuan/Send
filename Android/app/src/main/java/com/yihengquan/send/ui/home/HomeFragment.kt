package com.yihengquan.send.ui.home

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
        homeViewModel.setupServer(context)

        var binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            // Show keyboard automatically
            if (messageBox.requestFocus()) {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                messageBox.addTextChangedListener {
                    homeViewModel.setMessage(messageBox.text)
                }
            }

            sendFileButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                startActivityForResult(intent, 0)
            }

            sendImageButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            }

            homeViewModel.address.observe(viewLifecycleOwner, Observer {
                myAddressView.text = it
            })
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val takeFlags: Int? = (data?.flags?.and(
            (Intent.FLAG_GRANT_READ_URI_PERMISSION
            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        ))
        if (takeFlags != null) {
            context?.contentResolver?.takePersistableUriPermission(Uri.parse(data.dataString), takeFlags)
        }

        val contentString = data?.dataString;
        contentString?.let { homeViewModel.setMessage(it) }
        Log.i("HomeFragment", contentString.toString())
    }
}