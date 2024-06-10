package com.example.quikcart.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CouponModel (
    @SerializedName("price_rules")
    val priceRules: List<PriceRule>
)

@Entity("coupon")
data class PriceRule (
    @PrimaryKey val id: Long,
    @SerializedName("value_type")
    val valueType: String,
    val value: String,
)
 /*   @SerializedName("customer_selection")
    val customerSelection: String,

    @SerializedName("target_type")
    val targetType: String,

    @SerializedName("target_selection")
    val targetSelection: String,

    @SerializedName("allocation_method")
    val allocationMethod: String,

    @SerializedName("allocation_limit")
    val allocationLimit: JsonElement? = null,

    @SerializedName("once_per_customer")
    val oncePerCustomer: Boolean,

    @SerializedName("usage_limit")
    val usageLimit: JsonElement? = null,

    @SerializedName("starts_at")
    val startsAt: String,

    @SerializedName("ends_at")
    val endsAt: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("entitled_product_ids")
    val entitledProductIDS: JsonArray,

    @SerializedName("entitled_variant_ids")
    val entitledVariantIDS: JsonArray,

    @SerializedName("entitled_collection_ids")
    val entitledCollectionIDS: JsonArray,

    @SerializedName("entitled_country_ids")
    val entitledCountryIDS: JsonArray,

    @SerializedName("prerequisite_product_ids")
    val prerequisiteProductIDS: JsonArray,

    @SerializedName("prerequisite_variant_ids")
    val prerequisiteVariantIDS: JsonArray,

    @SerializedName("prerequisite_collection_ids")
    val prerequisiteCollectionIDS: JsonArray,

    @SerializedName("customer_segment_prerequisite_ids")
    val customerSegmentPrerequisiteIDS: JsonArray,

    @SerializedName("prerequisite_customer_ids")
    val prerequisiteCustomerIDS: JsonArray,

    @SerializedName("prerequisite_subtotal_range")
    val prerequisiteSubtotalRange: JsonElement? = null,

    @SerializedName("prerequisite_quantity_range")
    val prerequisiteQuantityRange: JsonElement? = null,

    @SerializedName("prerequisite_shipping_price_range")
    val prerequisiteShippingPriceRange: JsonElement? = null,

    @SerializedName("prerequisite_to_entitlement_quantity_ratio")
    val prerequisiteToEntitlementQuantityRatio: PrerequisiteToEntitlementQuantityRatio,

    @SerializedName("prerequisite_to_entitlement_purchase")
    val prerequisiteToEntitlementPurchase: PrerequisiteToEntitlementPurchase,

    val title: String,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlAPIID: String
)

@Serializable
data class PrerequisiteToEntitlementPurchase (
    @SerializedName("prerequisite_amount")
    val prerequisiteAmount: JsonElement? = null
)

@Serializable
data class PrerequisiteToEntitlementQuantityRatio (
    @SerializedName("prerequisite_quantity")
    val prerequisiteQuantity: JsonElement? = null,

    @SerializedName("entitled_quantity")
    val entitledQuantity: JsonElement? = null
)*/
