package com.example.quikcart.models.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomerRequest(
     val customer: Customer
 )


data class Customer(
    var addresses: List<Address>?=null,
    var password: String?=null,
    var password_confirmation: String?=null,
    var send_email_welcome: Boolean?=null,

    @field:SerializedName("note")
    val note: Any? = null,

    @field:SerializedName("tax_exempt")
    val taxExempt: Boolean? = null,

    @field:SerializedName("email_marketing_consent")
    val emailMarketingConsent: EmailMarketingConsent? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("multipass_identifier")
    val multipassIdentifier: Any? = null,

    @field:SerializedName("verified_email")
    val verifiedEmail: Boolean? = null,

    @field:SerializedName("tags")
    val tags: String? = null,

    @field:SerializedName("sms_marketing_consent")
    val smsMarketingConsent: SmsMarketingConsent? = null,

    @field:SerializedName("default_address")
    val defaultAddress: DefaultAddress? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("phone")
    var phone: String? = null,

    @field:SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String? = null,

    @field:SerializedName("tax_exemptions")
    val taxExemptions: List<Any?>? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("id")
    val id: Long? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,


)

data class Address(
    val address1: String,
    val city: String,
    val province: String,
    val phone: String,
    val zip: String,
    val last_name: String,
    val first_name: String,
    val country: String
)

data class CustomerResponse(
    val customers:List< Customers>
)

data class Customers(
    val id: Long,
    val email: String,
    @field:SerializedName("created_at") val createdAt: String,
    @field:SerializedName("updated_at") val updatedAt: String,
    @field:SerializedName("first_name") val firstName: String,
    @field:SerializedName("last_name") val lastName: String,
    @field:SerializedName("orders_count") val ordersCount: Int,
    val state: String,
    @field:SerializedName("total_spent") val totalSpent: String,
    @field:SerializedName("last_order_id") val lastOrderId: Long?,
    val note: String?,
    @field:SerializedName("verified_email") val verifiedEmail: Boolean,
    @field:SerializedName("multipass_identifier") val multipassIdentifier: String?,
    @field:SerializedName("tax_exempt") val taxExempt: Boolean,
    val tags: String,
    @field:SerializedName("last_order_name") val lastOrderName: String?,
    val currency: String,
    val phone: String?,
    val addresses: List<Address>,
    @field:SerializedName("tax_exemptions") val taxExemptions: List<Any>,
    @field:SerializedName("email_marketing_consent") val emailMarketingConsent: MarketingConsent,
    @field:SerializedName("sms_marketing_consent") val smsMarketingConsent: SmsMarketingConsent?,
    @field:SerializedName("admin_graphql_api_id") val adminGraphqlApiId: String,
    @field:SerializedName("default_address") val defaultAddress: AddressResponse?
)


data class AddressResponse(
    val id: Long?=null,
    val customer_id: Long?=null,
    val first_name: String?=null,
    val last_name: String?=null,
    val company: String?=null,
    val address1: String?=null,
    val address2: String?=null,
    val city: String?=null,
    val province: String?=null,
    val country: String?=null,
    val zip: String?=null,
    val phone: String?=null,
    val name: String?=null,
    val province_code: String?=null,
    val country_code: String?=null,
    val country_name: String?=null,
    val default: Boolean
):Serializable

data class MarketingConsent(
    val state: String,
    val opt_in_level: String,
    val consent_updated_at: String?
)

data class SmsMarketingConsent(
    val state: String,
    val opt_in_level: String,
    val consent_updated_at: String?,
    val consent_collected_from: String
)