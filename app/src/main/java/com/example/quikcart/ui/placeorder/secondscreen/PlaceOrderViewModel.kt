package com.example.quikcart.ui.placeorder.secondscreen

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Properties
import javax.inject.Inject
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@HiltViewModel
class PlaceOrderViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    lateinit var totalPrice: String
    lateinit var shippingFees: String
    lateinit var orderResponse: Order
    private val _uiState = MutableStateFlow<ViewState<Order>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<Order>> = _uiState

    private val _isLoading=MutableLiveData(false)
    var isLoading:LiveData<Boolean> =_isLoading

    fun confirmOrder() {
        viewModelScope.launch {
            _isLoading.value=true
            _uiState.value = ViewState.Loading
            repo.confirmOrder(orderResponse).catch {
                _isLoading.value=false
                _uiState.value = it.localizedMessage?.let { it1 -> ViewState.Error(it1) }!!
            }.collect { orderItems ->
                _isLoading.value=false
                _uiState.value = ViewState.Success(orderItems)
            }
        }
    }


    fun deleteCartItemsById(id:String){
        viewModelScope.launch {
            while (true){
                try {
                    repo.delCartItem(id)
                    break
                }catch (ex:Exception){
                    Log.e("TAG", "deleteCartItemsById: ${ex.localizedMessage}", )
                }
            }

        }
    }

     fun sendEmail(message:String) {
        try {

            val senderEmail = ""
            val password = ""


            val receiverEmail = ""

            val stringHost = "smtp.gmail.com"

            val properties = Properties().apply {
                put("mail.smtp.host", stringHost)
                put("mail.smtp.port", "465")
                put("mail.smtp.ssl.enable", "true")
                put("mail.smtp.auth", "true")
            }

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, password)
                }
            })

            val mimeMessage = MimeMessage(session).apply {
                addRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
                subject = "TEST#01"
                setText(message)
            }

            val t = Thread {
                try {
                    Transport.send(mimeMessage)
                    Handler(Looper.getMainLooper()).post {
                    }
                } catch (e: MessagingException) {
                    // Handling messaging exception
                    Handler(Looper.getMainLooper()).post {

                    }
                    Log.e("TAG", "sendEmail: ${e.message}", )
                    e.printStackTrace()
                }
            }
            t.start()
        } catch (e: AddressException) {
            Log.e("TAG", "sendEmail: ${e.message}", )
        } catch (e: MessagingException) {

            Log.e("TAG", "sendEmail: ${e.message}", )
        }

        Log.e("TAG", "sendEmail:Sent Successfully", )
    }
}