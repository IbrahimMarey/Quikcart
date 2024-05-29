package com.example.quikcart.models.entities


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ProductsResponse(

    @field:SerializedName("products")
    val products: List<ProductsItem>
)
data class ProductsItem(

    @field:SerializedName("image")
    val image: Image?,

    @field:SerializedName("body_html")
    val bodyHtml: String?,

    @field:SerializedName("images")
    val images: List<ImagesItem>?,

    @field:SerializedName("created_at")
    val createdAt: String?,

    @field:SerializedName("handle")
    val handle: String?,

    @field:SerializedName("variants")
    val variants: List<VariantsItem>?,

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("tags")
    val tags: String?,

    @field:SerializedName("published_scope")
    val publishedScope: String?,

    @field:SerializedName("product_type")
    val productType: String?,

    @field:SerializedName("template_suffix")
    val templateSuffix: Any?,

    @field:SerializedName("updated_at")
    val updatedAt: String?,

    @field:SerializedName("vendor")
    val vendor: String?,

    @field:SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String?,

    @field:SerializedName("options")
    val options: List<OptionsItem>?,

    @field:SerializedName("id")
    val id: Long?,

    @field:SerializedName("published_at")
    val publishedAt: String?,

    @field:SerializedName("status")
    val status: String?
) : Serializable

data class OptionsItem(

    @field:SerializedName("product_id")
    val productId: Long,

    @field:SerializedName("values")
    val values: List<String>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Long,

    @field:SerializedName("position")
    val position: Int
)
data class ImagesItem(

    @field:SerializedName("updated_at")
    val updatedAt: String?,

    @field:SerializedName("src")
    val src: String?,

    @field:SerializedName("product_id")
    val productId: Long?,

    @field:SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String?,

    @field:SerializedName("alt")
    val alt: Any?,

    @field:SerializedName("width")
    val width: Int?,

    @field:SerializedName("created_at")
    val createdAt: String?,

    @field:SerializedName("variant_ids")
    val variantIds: List<Any>?,

    @field:SerializedName("id")
    val id: Long?,

    @field:SerializedName("position")
    val position: Int?,

    @field:SerializedName("height")
    val height: Int?
)
data class VariantsItem(

    @field:SerializedName("inventory_management")
    val inventoryManagement: String?,

    @field:SerializedName("old_inventory_quantity")
    val oldInventoryQuantity: Int?,

    @field:SerializedName("requires_shipping")
    val requiresShipping: Boolean?,

    @field:SerializedName("created_at")
    val createdAt: String?,

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("updated_at")
    val updatedAt: String?,

    @field:SerializedName("inventory_item_id")
    val inventoryItemId: Long?,

    @field:SerializedName("price")
    val price: String?,

    @field:SerializedName("product_id")
    val productId: Long?,

    @field:SerializedName("option3")
    val option3: String?,

    @field:SerializedName("option1")
    val option1: String?,

    @field:SerializedName("id")
    val id: Long?,

    @field:SerializedName("option2")
    val option2: String?,

    @field:SerializedName("sku")
    val sku: String?,

    @field:SerializedName("grams")
    val grams: Int?,

    @field:SerializedName("barcode")
    val barcode: Any?,

    @field:SerializedName("inventory_quantity")
    val inventoryQuantity: Int?,

    @field:SerializedName("compare_at_price")
    val compareAtPrice: Any?,

    @field:SerializedName("fulfillment_service")
    val fulfillmentService: String?,

    @field:SerializedName("taxable")
    val taxable: Boolean?,

    @field:SerializedName("weight")
    val weight: Any?,

    @field:SerializedName("inventory_policy")
    val inventoryPolicy: String?,

    @field:SerializedName("weight_unit")
    val weightUnit: String?,

    @field:SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String?,

    @field:SerializedName("position")
    val position: Int?,

    @field:SerializedName("image_id")
    val imageId: Any?
)