package com.github.noman720.rxhloader.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.noman720.rxhloader.databinding.FragmentUserListBinding

/**
 * Created by Abu Noman on 7/21/19.
 */
class UserListFragment : Fragment() {

    private lateinit var viewModel: UserListViewModel
    private lateinit var factory: UserViewModelFactory

    private lateinit var mBinding: FragmentUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = UserViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, factory).get(UserListViewModel::class.java)

        //fetch vehicles data
        viewModel.fetchUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentUserListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        setupAdapter()
    }

    private fun setupAdapter(){
        mBinding.listViewVehicles.adapter = UserAdapter(UserAdapter.OnClickListener {
            // go to details
        })
    }

}
