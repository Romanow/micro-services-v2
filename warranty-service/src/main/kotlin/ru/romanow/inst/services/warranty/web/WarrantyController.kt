package ru.romanow.inst.services.warranty.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.romanow.inst.services.common.model.ErrorResponse
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import ru.romanow.inst.services.warranty.service.WarrantyService
import java.util.UUID
import javax.validation.Valid

@Tag(name = "Warranty API")
@RestController
@RequestMapping("/api/v1/warranty")
class WarrantyController(
    private val warrantyService: WarrantyService
) {

    @Operation(summary = "Check warranty status")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Warranty information"),
            ApiResponse(
                responseCode = "404",
                description = "Warranty info not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping(value = ["/{itemUid}"], produces = ["application/json"])
    fun warrantyInfo(@PathVariable itemUid: UUID): WarrantyInfoResponse {
        return warrantyService.getWarrantyInfo(itemUid)
    }

    @Operation(summary = "Start warranty period")
    @ApiResponse(responseCode = "204", description = "Warranty started for item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = ["/{itemUid}"])
    fun startWarranty(@PathVariable itemUid: UUID) {
        warrantyService.startWarranty(itemUid)
    }

    @Operation(summary = "Close warranty")
    @ApiResponse(responseCode = "204", description = "Warranty closed for item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemUid}")
    fun stopWarranty(@PathVariable itemUid: UUID) {
        warrantyService.stopWarranty(itemUid)
    }

    @Operation(summary = "Request warranty decision")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Warranty decision"),
            ApiResponse(
                responseCode = "400",
                description = "Bad request format",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Warranty not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping(value = ["/{itemUid}/warranty"], consumes = ["application/json"], produces = ["application/json"])
    fun warrantyRequest(
        @PathVariable itemUid: UUID,
        @Valid @RequestBody request: ItemWarrantyRequest
    ): OrderWarrantyResponse {
        return warrantyService.warrantyRequest(itemUid, request)
    }
}
