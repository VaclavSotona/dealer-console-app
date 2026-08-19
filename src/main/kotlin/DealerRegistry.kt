interface DealerRegistry {

    fun addDealer(
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Dealer

    fun updateDealer(
        id: Int,
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Boolean

    fun removeDealer(id: Int): Boolean

    fun dealerCount(): Int

    fun allDealers(): List<Dealer>
    fun findByNickname(nickname: String): List<Dealer>
    fun dealersByLoyalty(loyalty: Loyalty): List<Dealer>
    fun dealersWithMinIq(minIq: Int): List<Dealer>
    fun dealersWithIqInRange(minIq: Int, maxIq: Int): List<Dealer>
    fun dealersWithMinStrength(minStrength: Int): List<Dealer>
    fun dealersByTextInNickname(text: String): List<Dealer>
    fun eliteDealers(): List<Dealer>

    fun strongestDealer(): Dealer?
    fun smartestDealer(): Dealer?
    fun dealerWithMostYearsInPrison(): Dealer?
    fun smartestDealerFromLoyalty(loyalty: Loyalty): Dealer?
    fun dealerOfTheYear(): Dealer?
    fun findById(id: Int): Dealer?

    fun averageIq(): Double?
    fun averageStrength(): Double?
}