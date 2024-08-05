package com.example.userblinkitclone.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.Utils
import com.example.userblinkitclone.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        setStatusBarColor()
        getUserNumber()
        validateNumber()
        return binding.root
    }


    private fun validateNumber(){
        binding.btnContinue.setOnClickListener{
            val number = binding.etUserNumber.text.toString()

            if(number.isEmpty() || number.length !=10){
                Utils.showToast(requireContext(),"Please enter valid phone number")
            }
            else{
                val bundle = Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment,bundle)
            }
        }
    }


    private fun getUserNumber(){

        binding.etUserNumber.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = number?.length

                if(len==10){
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green))
                }
                else{
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(),R.color.yellow)
            statusBarColor = statusBarColors
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}