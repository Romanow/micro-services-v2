package ru.romanow.inst.services.store.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.store.model.*
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*

@Service
class StoreServiceImpl(
    private val userService: UserService,
    private val warehouseService: WarehouseService,
    private val orderService: OrderService,
    private val warrantyService: WarrantyService
) : StoreService {
    private val logger = LoggerFactory.getLogger(StoreServiceImpl::class.java)

    override fun findUserOrders(userId: UUID): UserOrdersResponse {
        TODO("not implemented")
    }

    override fun findUserOrder(userId: UUID, orderId: UUID): UserOrderResponse {
        TODO("not implemented")
    }

    override fun makePurchase(userId: UUID, request: PurchaseRequest): UUID {
        TODO("not implemented")
    }

    override fun refundPurchase(userId: UUID, orderId: UUID) {
        TODO("not implemented")
    }

    override fun warrantyRequest(userId: UUID, orderId: UUID, request: WarrantyRequest): WarrantyResponse {
        TODO("not implemented")
    }
}
