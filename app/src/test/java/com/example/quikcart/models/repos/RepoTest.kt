package com.example.quikcart.models.repos

import com.example.quikcart.fakes.FakeLocalDataSource
import com.example.quikcart.fakes.FakeRemoteDataSource
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.cart.CartCustomer
import com.example.quikcart.models.entities.cart.DraftItem
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepoTest {
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var repositoryImp: RepositoryImp

    @Before
    fun setUP()
    {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        repositoryImp = RepositoryImp(fakeRemoteDataSource,fakeLocalDataSource)
    }

    @Test
    fun PostDraftOrder_RetriveDraftOrder() = runBlockingTest{
        // Given
        val item = PostDraftOrderItemModel(
            DraftItem(
                line_items = listOf(DraftOrderLineItem( "title",  "2.00", 1)),
                applied_discount = null,
                customer = CartCustomer(1234567),
            )
        )

        // when
        repositoryImp.postCartItem(item).collect{
            //Then
            assertThat(it.draft_order.customer.id, `is`(1234567))
        }
    }

    @Test
    fun getDraftOrders() = runBlockingTest{
        // Given
        // when
        repositoryImp.getCartItems().collect{
            //Then
            assertThat(it.draftOrders[0].lineItems[0].title, `is`("Product 1"))
        }
    }

    @Test
    fun getAllCoupons() = runBlockingTest{
        // Given
        // when
        repositoryImp.getAllCoupons().collect{
            //Then
            assertThat(it[0].valueType, `is`("percentage"))
        }
    }


    @Test
    fun delAllCoupons() = runBlockingTest{
        // Given
        // when
        val del = repositoryImp.deleteAllCoupons()
            //Then
            assertThat(del, `is`(1))

    }

    @Test
    fun insertCoupon() = runBlockingTest{
        // Given
        // when
        val insert = repositoryImp.insertCoupon(
            PriceRule(
            id = 1,
            valueType = "percentage",
            value = "10"
        ))
        //Then
        assertThat(insert, `is`(1234567))

    }
}