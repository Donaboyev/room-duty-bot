package com.abbosidev.domain.admin

import com.abbosidev.infrastructure.util.withTransaction
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class AdminResource(
    private val adminService: AdminService,
) {
    @Query
    fun sendNotificationMessage() =
        withTransaction {
            adminService.sendNotificationMessage()
        }
}