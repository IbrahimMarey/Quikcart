package com.example.quikcart.models.entities

import com.google.gson.annotations.SerializedName

data class CustomerRequest(
     val customer: Customer
 )
data class Customer(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val verified_email: Boolean,
    val addresses: List<Address>,
    val password: String,
    val password_confirmation: String,
    val send_email_welcome: Boolean
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
    val id: Long,
    val customer_id: Long,
    val first_name: String,
    val last_name: String,
    val company: String?,
    val address1: String,
    val address2: String?,
    val city: String,
    val province: String,
    val country: String,
    val zip: String,
    val phone: String,
    val name: String,
    val province_code: String,
    val country_code: String,
    val country_name: String,
    val default: Boolean
)

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