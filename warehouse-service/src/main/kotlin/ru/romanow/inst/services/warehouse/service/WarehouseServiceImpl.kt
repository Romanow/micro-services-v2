package ru.romanow.inst.services.warehouse.service

import org.springframework.stereotype.Service
import ru.romanow.inst.services.warehouse.domain.OrderItem
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import ru.romanow.inst.services.warehouse.model.OrderItemRequest
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import java.util.*

@Service
class WarehouseServiceImpl : WarehouseService {
    override fun getItemInfo(itemId: UUID): ItemInfoResponse {
        TODO("not implemented")
    }

    override fun getOrderItem(itemId: UUID): OrderItem {
        TODO("not implemented")
    }

    override fun takeItem(request: OrderItemRequest): OrderItemResponse {
        TODO("not implemented")
    }

    override fun returnItem(orderItemUid: UUID) {
        TODO("not implemented")
    }

    override fun checkItemAvailableCount(itemId: UUID): Int {
        TODO("not implemented")
    }
}