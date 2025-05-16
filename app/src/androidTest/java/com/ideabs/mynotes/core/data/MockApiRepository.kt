package com.ideabs.mynotes.core.data

class MockAppRepository : ApiRepository {
    override suspend fun register(email: String, password: String): Result<Unit> {
        return if (email.endsWith("@test.com")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Mock error: invalid email"))
        }
    }
}