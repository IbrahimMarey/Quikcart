package com.example.quikcart.fakes

import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.BillingAddress
import com.example.quikcart.models.entities.CouponModel
import com.example.quikcart.models.entities.Customer
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.Customers
import com.example.quikcart.models.entities.EmailMarketingConsent
import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.ShippingAddress
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.entities.cart.AllDraftOrdersResponse
import com.example.quikcart.models.entities.cart.AppliedDiscount
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.DraftOrderResponse
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.models.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeRemoteDataSource:RemoteDataSource{
    override fun getBrands(): Flow<List<SmartCollectionsItem>> {
        TODO("Not yet implemented")
    }

    override fun getProductsByBrandId(id: Long): Flow<List<ProductsItem>> {
        TODO("Not yet implemented")
    }

    override fun getProductById(id: Long): Flow<ProductsItem> {
        TODO("Not yet implemented")
    }

    override fun getProductsBySubCategory(category: String): Flow<List<ProductsItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddress(
        customerID: Long,
        address: PostAddressModel
    ): Response<FetchAddress> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerAddresses(customerID: Long): Response<AddressesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun delCustomerAddress(customerID: Long, id: Long) {
        TODO("Not yet implemented")
    }

    override fun getProducts(): Flow<List<ProductsItem>> {
        TODO("Not yet implemented")
    }

    override fun getCustomer(): Flow<List<Customers>> {
        TODO("Not yet implemented")
    }

    override fun getCustomerOrders(customerID: Long): Flow<List<OrdersItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun postCartItem(cartItem: PostDraftOrderItemModel): Flow<PostDraftOrderItemModel> {
        return flow {
            val updatedCartItem = cartItem.copy(
                draft_order = cartItem.draft_order.copy(
                    line_items = cartItem.draft_order.line_items + DraftOrderLineItem(
                        title = "New Item",
                        price = "9.99",
                        quantity = 1
                    )
                )
            )
            emit(cartItem)
        }
    }

    override suspend fun getCartItems(): Flow<AllDraftOrdersResponse> {
        return flow {
            try {
                val draftOrders = getDraftOrdersFromLocalSource()

                val allDraftOrdersResponse = AllDraftOrdersResponse(draftOrders)

                emit(allDraftOrdersResponse)
            } catch (e: Exception) {
                throw Exception("Failed to retrieve cart items: ${e.message}")
            }
        }
    }

    override suspend fun delCartItem(id: String) {

    }

    override suspend fun postDraftOrder(draftOrderPostBody: PostDraftOrderItemModel): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun putDraftOrder(
        id: String,
        draftOrderPutBody: PutDraftOrderItemModel
    ): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDraftOrders(): Flow<AllDraftOrdersResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDraftOrderById(id: String): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCoupons(): Flow<CouponModel> {
        TODO("Not yet implemented")
    }

    override suspend fun confirmOrder(ordersItem: Order): Flow<Order> {
        TODO("Not yet implemented")
    }

    private fun getDraftOrdersFromLocalSource(): List<DraftOrder> {
        return listOf(
            DraftOrder(
                id = 1,
                note = null,
                email = "example@example.com",
                taxesIncluded = true,
                currency = "USD",
                invoiceSentAt = null,
                createdAt = "2023-06-20T12:34:56Z",
                updatedAt = "2023-06-20T12:34:56Z",
                taxExempt = false,
                completedAt = null,
                name = "Draft Order 1",
                status = "draft",
                lineItems = listOf(
                    LineItem(
                        id = 1,
                        variantID = null,
                        productID = null,
                        title = "Product 1",
                        variantTitle = null,
                        sku = null,
                        vendor = "Vendor 1",
                        quantity = 2,
                        requiresShipping = true,
                        taxable = true,
                        giftCard = false,
                        fulfillmentService = "manual",
                        grams = 100,
                        taxLines = emptyList(),
                        appliedDiscount = null,
                        name = "Product 1",
                        properties = emptyList(),
                        custom = false,
                        price = "10.00",
                        adminGraphqlAPIID = "gid://shopify/LineItem/1",
                        image = null
                    )
                ),
                shippingAddress = ShippingAddress(
                    firstName = "John",
                    lastName = "Doe",
                    address1 = "123 Main St",
                    phone = "555-1234",
                    city = "Anytown",
                    zip = "12345",
                    province = "CA",
                    country = "US",
                    address2 = null,
                    company = null,
                    latitude = null,
                    longitude = null,
                    name = "John Doe",
                    countryCode = null,
                    provinceCode = null,
                ),
                billingAddress = BillingAddress(
                    firstName = "John",
                    lastName = "Doe",
                    address1 = "123 Main St",
                    phone = "555-1234",
                    city = "Anytown",
                    zip = "12345",
                    province = "CA",
                    country = "US",
                    address2 = null,
                    company = null,
                    latitude = null,
                    longitude = null,
                    name = "John Doe",
                    countryCode = null,
                    provinceCode = null,
                ),
                invoiceURL = "https://example.com/invoice",
                appliedDiscount = AppliedDiscount(
                    description = "10% off",
                    value = "10",
                    title = "Discount",
                    amount = "1.00",
                    valueType = "percentage"
                ),
                orderID = null,
                shippingLine = null,
                taxLines = emptyList(),
                tags = "tag1, tag2",
                noteAttributes = emptyList(),
                totalPrice = "19.00",
                subtotalPrice = "18.00",
                totalTax = "1.00",
                paymentTerms = null,
                adminGraphqlAPIID = "gid://shopify/DraftOrder/1",
                customer = Customer(
                    id = 1,
                    email = "example@example.com",
                    createdAt = "2023-06-20T12:34:56Z",
                    updatedAt = "2023-06-20T12:34:56Z",
                    firstName = "John",
                    lastName = "Doe",
                    state = "active",
                    note = null,
                    verifiedEmail = true,
                    multipassIdentifier = null,
                    taxExempt = false,
                    tags = "customer-tag",
                    currency = "USD",
                    phone = null,
                    taxExemptions = emptyList(),
                    emailMarketingConsent = EmailMarketingConsent(
                        state = "subscribed",
                        optInLevel = "single_opt_in",
                        consentUpdatedAt = null
                    ),
                    smsMarketingConsent = null,
                    defaultAddress = null
                )
            )
        )
    }
}
