package com.example.quikcart.models.entities.cart

import com.example.quikcart.models.entities.BillingAddress
import com.example.quikcart.models.entities.Customer
import com.example.quikcart.models.entities.Image
import com.example.quikcart.models.entities.ShippingAddress

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllDraftOrdersResponse (
    @SerializedName("draft_orders")
    val draftOrders: List<DraftOrder>
)
data class DraftOrderResponse(
    val draft_order: DraftOrder
)
data class DraftOrder (
    val id: Long,
    val note: Any? = null,
    val email: String,

    @SerializedName("taxes_included")
    val taxesIncluded: Boolean,

    val currency: String,

    @SerializedName("invoice_sent_at")
    val invoiceSentAt: Any? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("tax_exempt")
    val taxExempt: Boolean,

    @SerializedName("completed_at")
    val completedAt: Any? = null,

    val name: String,
    val status: String,

    @SerializedName("line_items")
    val lineItems: List<LineItem>,

    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddress?=null,

    @SerializedName("billing_address")
    val billingAddress: BillingAddress?=null,

    @SerializedName("invoice_url")
    val invoiceURL: String,

    @SerializedName("applied_discount")
    val appliedDiscount: AppliedDiscount? = null,

    @SerializedName("order_id")
    val orderID: Any? = null,

    @SerializedName("shipping_line")
    val shippingLine: Any? = null,

    @SerializedName("tax_lines")
    val taxLines: List<Any?>,

    val tags: String,

    @SerializedName("note_attributes")
    val noteAttributes: List<Any?>,

    @SerializedName("total_price")
    val totalPrice: String,

    @SerializedName("subtotal_price")
    val subtotalPrice: String,

    @SerializedName("total_tax")
    val totalTax: String,

    @SerializedName("payment_terms")
    val paymentTerms: Any? = null,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlAPIID: String,

    val customer: Customer
):Serializable

data class AppliedDiscount (
    val description: String,
    val value: String,
    val title: String,
    val amount: String,

    @SerializedName("value_type")
    val valueType: String
)

/*data class Address (
    @SerializedName("first_name")
    val firstName: String,

    val address1: String,
    val phone: String,
    val city: String,
    val zip: String,
    val province: String,
    val country: String,

    @SerializedName("last_name")
    val lastName: String,

    val address2: String,
    val company: String,
    val latitude: Any? = null,
    val longitude: Any? = null,
    val name: String,

    @SerializedName("country_code")
    val countryCode: Any? = null,

    @SerializedName("province_code")
    val provinceCode: Any? = null,

    val id: Long? = null,

    @SerializedName("customer_id")
    val customerID: Long? = null,

    @SerializedName("country_name")
    val countryName: String? = null,

    val default: Boolean? = null
)*/

/*data class Customer (
    val id: Long,
    val email: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("orders_count")
    val ordersCount: Long,

    val state: String,

    @SerializedName("total_spent")
    val totalSpent: String,

    @SerializedName("last_order_id")
    val lastOrderID: Any? = null,

    val note: Any? = null,

    @SerializedName("verified_email")
    val verifiedEmail: Boolean,

    @SerializedName("multipass_identifier")
    val multipassIdentifier: Any? = null,

    @SerializedName("tax_exempt")
    val taxExempt: Boolean,

    val tags: String,

    @SerializedName("last_order_name")
    val lastOrderName: Any? = null,

    val currency: String,
    val phone: Any? = null,

    @SerializedName("tax_exemptions")
    val taxExemptions: List<Any?>,

    @SerializedName("email_marketing_consent")
    val emailMarketingConsent: EmailMarketingConsent,

    @SerializedName("sms_marketing_consent")
    val smsMarketingConsent: Any? = null,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlAPIID: String,

    @SerializedName("default_address")
    val defaultAddress: Address
)*/

data class EmailMarketingConsent (
    val state: String,

    @SerializedName("opt_in_level")
    val optInLevel: String,

    @SerializedName("consent_updated_at")
    val consentUpdatedAt: Any? = null
)

data class LineItem (
    val id: Long,

    @SerializedName("variant_id")
    val variantID: Any? = null,

    @SerializedName("product_id")
    val productID: Any? = null,

    val title: String,

    @SerializedName("variant_title")
    val variantTitle: Any? = null,

    val sku: Any? = null,
    val vendor: String? = null,
    var quantity: Long,

    @SerializedName("requires_shipping")
    val requiresShipping: Boolean,

    val taxable: Boolean,

    @SerializedName("gift_card")
    val giftCard: Boolean,

    @SerializedName("fulfillment_service")
    var fulfillmentService: String,

    var grams: Long,

    @SerializedName("tax_lines")
    val taxLines: List<Any?>,

    @SerializedName("applied_discount")
    val appliedDiscount: Any? = null,

    val name: String,
    val properties: List<Any?>,
    val custom: Boolean,
    var price: String,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlAPIID: String,
    var image: Image? = null
)
