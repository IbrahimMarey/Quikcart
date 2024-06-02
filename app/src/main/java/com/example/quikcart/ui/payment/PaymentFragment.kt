package com.example.quikcart.ui.payment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quikcart.BuildConfig
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentAddressesBinding
import com.example.quikcart.databinding.FragmentPaymentBinding
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import java.math.BigDecimal

private const val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"/*"com.google.android.apps.nbu.paisa.user"*//*BuildConfig.APPLICATION_ID*/
private const val GOOGLE_PAY_REQUEST_CODE = 123
//paypal
private const val PAYPAL_CLIENT_ID = "AfR2ylX7Lxzx92G30PzuibgSS0tIPLGNlFy0ove_c7tEzoxGjOfGkL0MhMoPHimdP7n-rqPaHGtDGirp"
private const val PAYPAL_REQUEST_CODE = 7171
private val paypalConfig = PayPalConfiguration()
    .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
    .clientId(PAYPAL_CLIENT_ID)
class PaymentFragment : Fragment() {
    lateinit var binding : FragmentPaymentBinding
    lateinit var uri :Uri
    lateinit var status:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPayPalService()
     }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        paypalActivityResult(requestCode, resultCode, data)
        gPayResult(requestCode, resultCode, data)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paypalPayCard.setOnClickListener{
            payWithPayPal()
        }
        binding.googlePayCard.setOnClickListener {
            var name = "MRUDUL K M" /*"MUHAMMED HASHIM"*/
            var upiID ="mr18yt@okaxis"/*"hashimasd123@oksbi"*/
            var transaction ="pay test"
            uri = getUpiPaymentUri(name,upiID,transaction,"0.1")
            payWithGPay()
        }
    }


    // Google Pay Functions Start
    private fun gPayResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == GOOGLE_PAY_REQUEST_CODE)
        {
            if (data !=null)
            {
                status = data.getStringExtra("Status")?.lowercase() ?: ""
            }

            if (RESULT_OK == resultCode && status.equals("success"))
            {
                Toast.makeText(requireActivity(), "Transaction Successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireActivity(), "Transaction Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun payWithGPay()
    {
        if (isAppInstalled(activity ?: requireContext(), GOOGLE_PAY_PACKAGE_NAME))
        {
            var intent = Intent(Intent.ACTION_VIEW)
            intent.setData(uri)
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
        }else
        {
            Toast.makeText(requireActivity(), "Please Install GPay", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isAppInstalled(ctx:Context,packageName:String):Boolean
    {
        return try {

//        requireActivity().packageManager.getApplicationInfo(packageName,0)
            Log.i("TAG", "isAppInstalled: ==  ${ctx.packageManager.getPackageInfo(GOOGLE_PAY_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)}")
            val info = ctx.packageManager.getPackageInfo(GOOGLE_PAY_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            Log.i("TAG", "isAppInstalled: $info")
            info != null
        }catch (e:PackageManager.NameNotFoundException){
             false
        }
    }
    private fun getUpiPaymentUri(name:String,upiID:String,transactionNote:String,amount:String):Uri
    {
        return Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa",upiID)
            .appendQueryParameter("pn",name)
            .appendQueryParameter("tn",transactionNote)
            .appendQueryParameter("am",amount)
            .appendQueryParameter("cu","USD")
            .build()
    }
    // Google Pay End Functions End


    // PayPal Functions Start
    private fun payWithPayPal()
    {
        var payment = PayPalPayment(BigDecimal("0.01"),"USD","Donate for EDMTDev",PayPalPayment.PAYMENT_INTENT_SALE)
        var intent = Intent(requireActivity(),PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }
    private fun startPayPalService()
    {
        val intent = Intent(requireActivity(),PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        requireActivity().startService(intent)
    }
    private fun stopPayPalService()
    {
        requireActivity().stopService(Intent(requireActivity(),PayPalService::class.java))
    }
    private fun paypalActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (requestCode == RESULT_OK)
            {
                var confirmation = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirmation != null)
                {
                    var d = confirmation.toJSONObject().toString(4)
                    Log.i("TAG", "onActivityResult: ${d}")
                }
            }else if (requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(requireActivity(), getString(R.string.cancel), Toast.LENGTH_SHORT).show()
            }
        }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(requireActivity(), getString(R.string.delete), Toast.LENGTH_SHORT).show()
        }
    }

    // PayPal Functions End
    override fun onDestroyView() {
        super.onDestroyView()
        stopPayPalService()
    }
}