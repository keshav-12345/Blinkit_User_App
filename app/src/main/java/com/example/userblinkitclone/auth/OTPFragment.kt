package com.example.userblinkitclone.auth

import  android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.models.Users
import com.example.userblinkitclone.Utils
import com.example.userblinkitclone.activity.UsersMainActivity
import com.example.userblinkitclone.databinding.FragmentOTPBinding
import com.example.userblinkitclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var userNumber:String
    private lateinit var binding : FragmentOTPBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)
        getUserNumber()
        CheckingEnteringOtp()
        onBackButtonClicked()
        sendOTP()
        onLoginButtonClicked()
        return binding.root
    }

    private fun onLoginButtonClicked() {
        binding.btnLogin.setOnClickListener{
            Utils.showDialog(requireContext(),"Signing you... ")
            val editTexts = arrayOf(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.etOtp5,binding.etOtp6)
            val otp = editTexts.joinToString(""){it.text.toString()}
//            Log.e("merc","Otp $otp")

            if(otp.length < editTexts.size){
                Utils.showToast(requireContext(),"Please enter valid Otp")
            }
            verifyOtp(otp)
        }
    }

    private fun verifyOtp(otp: String) {

        val user = Users(uid= null , userPhoneNumber = userNumber , userAddress = null )

        viewModel.signInWithPhoneAuthCredential(otp,userNumber,user)

        lifecycleScope.launch {
            viewModel.isSignedInSuccesfully.collect{
                if(it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"Logged In")
                    startActivity(Intent(requireActivity(),UsersMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(),"Sending OTP")
        viewModel.apply {
            sendOTP(userNumber,requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if(it){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(),"Otp sent...")
                    }
                }
            }

        }

    }

    private fun onBackButtonClicked(){
        binding.tbOtpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun CheckingEnteringOtp(){
        val editTexts = arrayOf(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.etOtp5,binding.etOtp6)
        for(i in editTexts.indices){
            editTexts[i].addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if(s?.length == 1){
                        if(i < editTexts.size-1){
                            editTexts[i+1].requestFocus()
                        }
                    }
                    else if(s?.length == 0){
                        if(i>0){
                            editTexts[i-1].requestFocus()
                        }
                    }
                }

            })
        }
    }

    private fun getUserNumber(){
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()

        binding.tvUserNumber.text = userNumber
    }
}