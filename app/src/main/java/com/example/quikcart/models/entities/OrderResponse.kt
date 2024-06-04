package com.example.quikcart.models.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderResponse(

	@field:SerializedName("orders")
	val orders: List<OrdersItem?>? = null
)

data class CurrentTotalTaxSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

/*data class Customer(

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
	val phone: String? = null,

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

	var addresses: List<Address>?=null,
	var password: String?=null,
	var password_confirmation: String?=null,
	var send_email_welcome: Boolean?=null
)*/

data class CurrentSubtotalPriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class LineItemsItem(

	@field:SerializedName("variant_title")
	val variantTitle: Any? = null,

	@field:SerializedName("fulfillment_status")
	val fulfillmentStatus: Any? = null,

	@field:SerializedName("total_discount")
	val totalDiscount: String? = null,

	@field:SerializedName("gift_card")
	val giftCard: Boolean? = null,

	@field:SerializedName("requires_shipping")
	val requiresShipping: Boolean? = null,

	@field:SerializedName("total_discount_set")
	val totalDiscountSet: TotalDiscountSet? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("attributed_staffs")
	val attributedStaffs: List<Any?>? = null,

	@field:SerializedName("product_exists")
	val productExists: Boolean? = null,

	@field:SerializedName("variant_id")
	val variantId: Any? = null,

	@field:SerializedName("tax_lines")
	val taxLines: List<TaxLinesItem?>? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("vendor")
	val vendor: String? = null,

	@field:SerializedName("product_id")
	val productId: Any? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("grams")
	val grams: Int? = null,

	@field:SerializedName("sku")
	val sku: Any? = null,

	@field:SerializedName("fulfillable_quantity")
	val fulfillableQuantity: Int? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("fulfillment_service")
	val fulfillmentService: String? = null,

	@field:SerializedName("taxable")
	val taxable: Boolean? = null,

	@field:SerializedName("variant_inventory_management")
	val variantInventoryManagement: Any? = null,

	@field:SerializedName("discount_allocations")
	val discountAllocations: List<Any?>? = null,

	@field:SerializedName("current_quantity")
	val currentQuantity: Int? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("price_set")
	val priceSet: PriceSet? = null,

	@field:SerializedName("properties")
	val properties: List<Any?>? = null,

	@field:SerializedName("duties")
	val duties: List<Any?>? = null
)

data class EmailMarketingConsent(

	@field:SerializedName("consent_updated_at")
	val consentUpdatedAt: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("opt_in_level")
	val optInLevel: String? = null
)

data class TotalShippingPriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class TotalLineItemsPriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class PresentmentMoney(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("currency_code")
	val currencyCode: String? = null
)

data class TotalDiscountSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class BillingAddress(

	@field:SerializedName("zip")
	val zip: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address2")
	val address2: Any? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("province_code")
	val provinceCode: Any? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("province")
	val province: Any? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("company")
	val company: Any? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class TotalDiscountsSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class TaxLinesItem(

	@field:SerializedName("channel_liable")
	val channelLiable: Boolean? = null,

	@field:SerializedName("rate")
	val rate: Any? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("price_set")
	val priceSet: PriceSet? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class OrdersItem(

	@field:SerializedName("cancelled_at")
	val cancelledAt: Any? = null,

	@field:SerializedName("confirmation_number")
	val confirmationNumber: String? = null,

	@field:SerializedName("fulfillment_status")
	val fulfillmentStatus: Any? = null,

	@field:SerializedName("original_total_additional_fees_set")
	val originalTotalAdditionalFeesSet: Any? = null,

	@field:SerializedName("current_total_discounts_set")
	val currentTotalDiscountsSet: CurrentTotalDiscountsSet? = null,

	@field:SerializedName("billing_address")
	val billingAddress: Any? = null,

	@field:SerializedName("line_items")
	val lineItems: List<LineItemsItem?>? = null,

	@field:SerializedName("original_total_duties_set")
	val originalTotalDutiesSet: Any? = null,

	@field:SerializedName("presentment_currency")
	val presentmentCurrency: String? = null,

	@field:SerializedName("total_discounts_set")
	val totalDiscountsSet: TotalDiscountsSet? = null,

	@field:SerializedName("location_id")
	val locationId: Any? = null,

	@field:SerializedName("source_url")
	val sourceUrl: Any? = null,

	@field:SerializedName("landing_site")
	val landingSite: Any? = null,

	@field:SerializedName("source_identifier")
	val sourceIdentifier: Any? = null,

	@field:SerializedName("reference")
	val reference: Any? = null,

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("checkout_id")
	val checkoutId: Any? = null,

	@field:SerializedName("checkout_token")
	val checkoutToken: Any? = null,

	@field:SerializedName("tax_lines")
	val taxLines: List<TaxLinesItem?>? = null,

	@field:SerializedName("current_total_discounts")
	val currentTotalDiscounts: String? = null,

	@field:SerializedName("merchant_of_record_app_id")
	val merchantOfRecordAppId: Any? = null,

	@field:SerializedName("customer_locale")
	val customerLocale: Any? = null,

	@field:SerializedName("current_total_additional_fees_set")
	val currentTotalAdditionalFeesSet: Any? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("app_id")
	val appId: Long? = null,

	@field:SerializedName("subtotal_price")
	val subtotalPrice: String? = null,

	@field:SerializedName("closed_at")
	val closedAt: Any? = null,

	@field:SerializedName("order_status_url")
	val orderStatusUrl: String? = null,

	@field:SerializedName("current_total_price_set")
	val currentTotalPriceSet: CurrentTotalPriceSet? = null,

	@field:SerializedName("device_id")
	val deviceId: Any? = null,

	@field:SerializedName("test")
	val test: Boolean? = null,

	@field:SerializedName("total_shipping_price_set")
	val totalShippingPriceSet: TotalShippingPriceSet? = null,

	@field:SerializedName("subtotal_price_set")
	val subtotalPriceSet: SubtotalPriceSet? = null,

	@field:SerializedName("tax_exempt")
	val taxExempt: Boolean? = null,

	@field:SerializedName("payment_gateway_names")
	val paymentGatewayNames: List<String?>? = null,

	@field:SerializedName("total_tax")
	val totalTax: String? = null,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("current_subtotal_price_set")
	val currentSubtotalPriceSet: CurrentSubtotalPriceSet? = null,

	@field:SerializedName("current_total_tax")
	val currentTotalTax: String? = null,

	@field:SerializedName("shipping_lines")
	val shippingLines: List<Any?>? = null,

	@field:SerializedName("phone")
	val phone: Any? = null,

	@field:SerializedName("user_id")
	val userId: Any? = null,

	@field:SerializedName("note_attributes")
	val noteAttributes: List<Any?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("cart_token")
	val cartToken: Any? = null,

	@field:SerializedName("total_tax_set")
	val totalTaxSet: TotalTaxSet? = null,

	@field:SerializedName("landing_site_ref")
	val landingSiteRef: Any? = null,

	@field:SerializedName("discount_codes")
	val discountCodes: List<Any?>? = null,

	@field:SerializedName("estimated_taxes")
	val estimatedTaxes: Boolean? = null,

	@field:SerializedName("note")
	val note: Any? = null,

	@field:SerializedName("current_subtotal_price")
	val currentSubtotalPrice: String? = null,

	@field:SerializedName("current_total_tax_set")
	val currentTotalTaxSet: CurrentTotalTaxSet? = null,

	@field:SerializedName("total_outstanding")
	val totalOutstanding: String? = null,

	@field:SerializedName("order_number")
	val orderNumber: Int? = null,

	@field:SerializedName("discount_applications")
	val discountApplications: List<Any?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_line_items_price_set")
	val totalLineItemsPriceSet: TotalLineItemsPriceSet? = null,

	@field:SerializedName("taxes_included")
	val taxesIncluded: Boolean? = null,

	@field:SerializedName("buyer_accepts_marketing")
	val buyerAcceptsMarketing: Boolean? = null,

	@field:SerializedName("payment_terms")
	val paymentTerms: Any? = null,

	@field:SerializedName("confirmed")
	val confirmed: Boolean? = null,

	@field:SerializedName("total_weight")
	val totalWeight: Int? = null,

	@field:SerializedName("contact_email")
	val contactEmail: String? = null,

	@field:SerializedName("refunds")
	val refunds: List<Any?>? = null,

	@field:SerializedName("total_discounts")
	val totalDiscounts: String? = null,

	@field:SerializedName("fulfillments")
	val fulfillments: List<Any?>? = null,

	@field:SerializedName("client_details")
	val clientDetails: Any? = null,

	@field:SerializedName("po_number")
	val poNumber: Any? = null,

	@field:SerializedName("referring_site")
	val referringSite: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("processed_at")
	val processedAt: String? = null,

	@field:SerializedName("company")
	val company: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("shipping_address")
	val shippingAddress: Any? = null,

	@field:SerializedName("browser_ip")
	val browserIp: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("source_name")
	val sourceName: String? = null,

	@field:SerializedName("total_price_set")
	val totalPriceSet: TotalPriceSet? = null,

	@field:SerializedName("current_total_duties_set")
	val currentTotalDutiesSet: Any? = null,

	@field:SerializedName("total_price")
	val totalPrice: String? = null,

	@field:SerializedName("total_line_items_price")
	val totalLineItemsPrice: String? = null,

	@field:SerializedName("total_tip_received")
	val totalTipReceived: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("cancel_reason")
	val cancelReason: Any? = null,

	@field:SerializedName("current_total_price")
	val currentTotalPrice: String? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String? = null,

	@field:SerializedName("financial_status")
	val financialStatus: String? = null,

	@field:SerializedName("customer")
	val customer: Customer? = null
):Serializable

data class TotalPriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class PriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class SubtotalPriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class TotalTaxSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

/*data class SmsMarketingConsent(

	@field:SerializedName("consent_updated_at")
	val consentUpdatedAt: Any? = null,

	@field:SerializedName("consent_collected_from")
	val consentCollectedFrom: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("opt_in_level")
	val optInLevel: String? = null
)*/

data class ShippingAddress(

	@field:SerializedName("zip")
	val zip: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address2")
	val address2: Any? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("province_code")
	val provinceCode: Any? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("province")
	val province: Any? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("company")
	val company: Any? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class DefaultAddress(

	@field:SerializedName("zip")
	val zip: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("address2")
	val address2: Any? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("province_code")
	val provinceCode: Any? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("default")
	val jsonMemberDefault: Boolean? = null,

	@field:SerializedName("province")
	val province: Any? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("country_name")
	val countryName: String? = null,

	@field:SerializedName("company")
	val company: Any? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("customer_id")
	val customerId: Long? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null
)

data class CurrentTotalDiscountsSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)

data class ShopMoney(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("currency_code")
	val currencyCode: String? = null
)

data class CurrentTotalPriceSet(

	@field:SerializedName("shop_money")
	val shopMoney: ShopMoney? = null,

	@field:SerializedName("presentment_money")
	val presentmentMoney: PresentmentMoney? = null
)
