package com.example.quikcart.ui.home

import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CategoryItem
import com.example.quikcart.models.entities.CouponModel
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.Customers
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.entities.cart.AllDraftOrdersResponse
import com.example.quikcart.models.entities.cart.DraftOrderResponse
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.models.repos.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import retrofit2.Response

class FakeRepo : Repository {
    private val categories = listOf(
        CategoryItem(name = "Category 1", img = 0),
        CategoryItem( name = "Category 2", img = 0)
    )
    val smartCollections = listOf(SmartCollectionsItem(id = 1), SmartCollectionsItem(id = 2))
    private val _coupons = MutableStateFlow(
        CouponModel(
            listOf(
                PriceRule(id = 1, value = "-20.0", valueType = "percentage"),
                PriceRule(id = 2, value = "-10.0", valueType = "fixed_amount")
            )
        )
    )
    val coupons: CouponModel get() = _coupons.value
    override fun getBrands(): Flow<List<SmartCollectionsItem>> {
        return flowOf(smartCollections)
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

    override suspend fun getAllAddressesShopify(customerID: Long): Flow<AddressesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddressShopify(customerID: Long, address: PostAddressModel) {
        TODO("Not yet implemented")
    }

    override suspend fun delAddressShopify(customerID: Long, id: Long) {
        TODO("Not yet implemented")
    }

    override fun getProducts(): Flow<List<ProductsItem>> {
        TODO("Not yet implemented")
    }

    override fun getCategories(): List<CategoryItem> {
        return categories
    }

    override fun getCustomer(): Flow<List<Customers>> {
        TODO("Not yet implemented")
    }

    override fun getCustomerOrders(customerID: Long): Flow<List<OrdersItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun postCartItem(cartItem: PostDraftOrderItemModel): Flow<PostDraftOrderItemModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartItems(): Flow<AllDraftOrdersResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun delCartItem(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun inertProduct(product: ProductsItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(product: ProductsItem) {
        TODO("Not yet implemented")
    }

    override fun getAllProducts(): Flow<List<ProductsItem>> {
        TODO("Not yet implemented")
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
        return flowOf(coupons)
    }

    override suspend fun insertCoupon(coupon: PriceRule): Long {
        _coupons.update { currentCoupons ->
            CouponModel(currentCoupons.priceRules + coupon)
        }
        return coupon.id
    }

    override suspend fun deleteAllCoupons(): Int {
        val count = _coupons.value.priceRules.size
        _coupons.update { CouponModel(emptyList()) }
        return count
    }

    override suspend fun insertAllCoupons(coupons: List<PriceRule>): LongArray {
        _coupons.update { CouponModel(coupons) }
        return coupons.map { it.id.toLong() }.toLongArray()
    }

    override suspend fun getAllCoupons(): Flow<List<PriceRule>> {
        TODO("Not yet implemented")
    }

    override suspend fun confirmOrder(ordersItem: Order): Flow<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllProducts() {
        TODO("Not yet implemented")
    }

}