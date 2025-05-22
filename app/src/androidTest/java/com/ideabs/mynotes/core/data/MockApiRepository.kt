package com.ideabs.mynotes.core.data

class MockAppRepository : ApiRepository {
    override suspend fun register(email: String, password: String): Result<Unit> {
        return if (email.endsWith("@test.com")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Mock error: invalid email"))
        }
    }

    override suspend fun login(email: String, password: String): Result<RemoteApiRepository.TokenData> {
        return if (email == "test@test.com" && password == "Password#0"){
            Result.success(RemoteApiRepository.TokenData("","",""))
        } else {
            Result.failure(Exception("Mock error: login failed"))
        }
    }
}