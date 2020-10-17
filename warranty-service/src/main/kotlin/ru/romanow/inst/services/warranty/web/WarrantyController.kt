package ru.romanow.inst.services.warranty.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import ru.romanow.inst.services.warranty.service.WarrantyService
import java.util.*
import javax.validation.Valid

@Tag(name = "Warranty API")
@RestController
@RequestMapping("/api/v1/warranty")
class WarrantyController(
    private val warrantyService: WarrantyService
) {

    @Operation(summary = "Check warranty status")
    @ApiResponse(responseCode = "200", description = "Warranty information")
    @GetMapping(value = ["/{itemId}"], produces = ["application/json"])
    fun warrantyInfo(@PathVariable itemId: UUID): WarrantyInfoResponse {
        return warrantyService.getWarrantyInfo(itemId)
    }

    @Operation(summary = "Start warranty period")
    @ApiResponse(responseCode = "201", description = "Start warranty for item")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = ["/{itemId}"])
    fun startWarranty(@PathVariable itemId: UUID) {
        warrantyService.startWarranty(itemId)
    }

    @Operation(summary = "Close warranty")
    @ApiResponse(responseCode = "204", description = "Close warranty for item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    fun stopWarranty(@PathVariable itemId: UUID) {
        warrantyService.stopWarranty(itemId)
    }

    @Operation(summary = "Request warranty decision")
    @ApiResponse(responseCode = "200", description = "Warranty decision")
    @PostMapping(value = ["/{itemId}/warranty"], consumes = ["application/json"], produces = ["application/json"])
    fun warrantyRequest(@PathVariable itemId: UUID, @Valid @RequestBody request: ItemWarrantyRequest): OrderWarrantyResponse {
        return warrantyService.warrantyRequest(itemId, request)
    }
}