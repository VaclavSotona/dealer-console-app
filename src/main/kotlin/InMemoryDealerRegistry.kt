class InMemoryDealerRegistry : DealerRegistry {

    private val dealers = mutableListOf<Dealer>()
    private var nextId = 1

    override fun addDealer(
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Dealer {

        val dealer = Dealer(
            nextId++,
            nickname,
            yearsInPrison,
            loyalty,
            strength,
            iq
        )

        dealers.add(dealer)
        return dealer
    }

    override fun updateDealer(
        id: Int,
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Boolean {

        val dealer = findById(id) ?: return false

        dealer.nickname = nickname
        dealer.yearsInPrison = yearsInPrison
        dealer.loyalty = loyalty
        dealer.strength = strength
        dealer.iq = iq

        return true
    }

    override fun removeDealer(id: Int): Boolean {
        return dealers.removeIf { it.id == id }
    }

    override fun dealerCount(): Int {
        return dealers.size
    }

    override fun allDealers(): List<Dealer> {
        return dealers
    }

    override fun findByNickname(nickname: String): List<Dealer> {
        return dealers.filter {
            it.nickname.equals(nickname, ignoreCase = true)
        }
    }

    override fun dealersByLoyalty(loyalty: Loyalty): List<Dealer> {
        return dealers.filter { it.loyalty == loyalty }
    }

    override fun dealersWithMinIq(minIq: Int): List<Dealer> {
        return dealers.filter { it.iq >= minIq }
    }

    override fun dealersWithIqInRange(minIq: Int, maxIq: Int): List<Dealer> {
        return dealers.filter { it.iq in minIq..maxIq }
    }

    override fun dealersWithMinStrength(minStrength: Int): List<Dealer> {
        return dealers.filter { it.strength >= minStrength }
    }

    override fun dealersByTextInNickname(text: String): List<Dealer> {
        return dealers.filter { it.nickname.contains(text, ignoreCase = true) }
    }

    override fun eliteDealers(): List<Dealer> {
        return dealers
            .filter { it.iq >= 110 }
            .filter { it.loyalty == Loyalty.INNER_CIRCLE}
    }


    override fun strongestDealer(): Dealer? {
        return dealers.maxByOrNull { it.strength }
    }

    override fun smartestDealer(): Dealer? {
        return dealers.maxByOrNull { it.iq }
    }

    override fun dealerWithMostYearsInPrison(): Dealer? {
        return dealers.maxByOrNull { it.yearsInPrison }
    }

    override fun smartestDealerFromLoyalty(loyalty: Loyalty): Dealer? {
        return dealers
            .filter { it.loyalty == loyalty }
            .maxByOrNull { it.iq }
    }

    override fun dealerOfTheYear(): Dealer? {
        return dealers.maxByOrNull { it.iq + it.strength }
    }

    override fun findById(id: Int): Dealer? {
        return dealers.find { it.id == id }
    }

    override fun averageIq(): Double? {
        return dealers.map { it.iq }.average()
    }

    override fun averageStrength(): Double? {
        return dealers.map { it.strength }.average()
    }
}
