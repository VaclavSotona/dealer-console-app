const val MIN_IQ = 50
const val MAX_IQ = 160
const val MIN_STRENGTH = 1
const val MAX_STRENGTH = 100

val menuItems = listOf(
    "1. Přijmout nového dealera",
    "2. Upravit údaje dealera",
    "3. Vyhodit dealera z organizace",
    "4. Zobrazit počet dealerů",
    "5. Vypsat všechny dealery",
    "6. Najít dealera podle přezdívky",
    "7. Vypsat dealery podle loajality",
    "8. Vypsat dealery s minimálním IQ",
    "9. Vypsat dealery s IQ v rozmezí",
    "10. Vypsat dealery s minimální silou",
    "11. Najít dealery podle textu v přezdívce",
    "12. Vypsat elite dealery",
    "13. Zobrazit nejsilnějšího dealera",
    "14. Zobrazit nejchytřejšího dealera",
    "15. Mukl roku",
    "16. Nejchytřejší dealer z loajality",
    "17. Najít nejlepšího dealera",
    "18. Najít dealera podle ID",
    "19. Průměrné IQ",
    "20. Průměrná síla",
    "0. Zavřít databázi Zdenda Crew"
)
fun showMenu() {
    println()
    println("================================================")
    println("              ZDENDA CREW DATABASE")
    println("================================================")
    println()
    val leftColumnSize = (menuItems.size + 1) / 2
    val leftColumn = menuItems.take(leftColumnSize)
    val rightColumn = menuItems.drop(leftColumnSize)
    for (i in leftColumn.indices) {
        val left = leftColumn[i].padEnd(45)
        val right = rightColumn.getOrNull(i) ?: ""
        println(left + right)
    }
    println()
    print("Vyber možnost: ")
}

fun showGoodbyeMessage() {
    println()
    println("================================================")
    println("      Databáze Zdenda Crew byla uzavřena")
    println("================================================")
}
fun printDealer(dealer: Dealer) {
    println(
        "ID: ${dealer.id} | " +
                "Přezdívka: ${dealer.nickname} | " +
                "Vězení: ${dealer.yearsInPrison} let | " +
                "Loajalita: ${dealer.loyalty} | " +
                "Síla: ${dealer.strength} | " +
                "IQ: ${dealer.iq}"
    )
}
fun printDealers(dealers: List<Dealer>) {
    dealers.forEach(::printDealer)
}

fun readNonBlankText(prompt: String): String {
    while (true) {
        print(prompt)
        val value = readln()
        if (!value.isBlank()) {
            return value
        }
        println("Chyba: Hodnota nesmí být prázdná.")
    }
}
fun readInt(prompt: String): Int {
    while (true) {
        print(prompt)
        val value = readln().toIntOrNull()
        if (value != null) {
            return value
        }
        println("Chyba: Neplatné číslo, zkus to znovu.")
    }
}
fun readIntInRange(prompt: String, min: Int, max: Int): Int {
    while (true) {
        val value = readInt(prompt)
        if (value in min..max) {
            return value
        }
        println("Chyba: Povolený rozsah je $min až $max.")
    }
}
fun readYearsInPrison(): Int {
    while (true) {
        val years = readInt("Roky ve vězení: ")
        if (years >= 0) {
            return years
        }
        println("Chyba: Musí být 0 nebo více.")
    }
}
fun readLoyalty(): Loyalty {
    while (true) {
        println("Loajalita — vyber číslo:")
        Loyalty.entries.forEachIndexed { index, loyalty ->
            println("${index + 1}. $loyalty")
        }
        print("Volba: ")
        val choice = readln().toIntOrNull()
        if (choice != null &&
            choice in 1..Loyalty.entries.size
        ) {
            return Loyalty.entries[choice - 1]
        }
        println("Neplatná volba, zkus to znovu.")
    }
}

fun findExistingDealer(registry: DealerRegistry): Dealer? {
    val id = readInt("Zadej ID dealera: ")
    val dealer = registry.findById(id)
    if (dealer == null) {
        println("Dealer s ID $id neexistuje.")
    }
    return dealer
}

fun handleAddDealer(registry: DealerRegistry) {
    val nickname = readNonBlankText("Přezdívka: ")
    val yearsInPrison = readYearsInPrison()
    val loyalty = readLoyalty()
    val strength = readIntInRange(
        "Fyzická síla ($MIN_STRENGTH-$MAX_STRENGTH): ",
        MIN_STRENGTH,
        MAX_STRENGTH
    )
    val iq = readIntInRange(
        "IQ ($MIN_IQ-$MAX_IQ): ",
        MIN_IQ,
        MAX_IQ
    )
    val dealer = registry.addDealer(
        nickname,
        yearsInPrison,
        loyalty,
        strength,
        iq
    )
    println()
    println(
        "Dealer ${dealer.nickname} přijat do Zdenda Crew s ID ${dealer.id}."
    )
}
fun handleEditDealer(registry: DealerRegistry) {
    val dealer = findExistingDealer(registry) ?: return
    println()
    println("Úprava dealera:")
    printDealer(dealer)
    val nickname = readNonBlankText("Nová přezdívka: ")
    val yearsInPrison = readYearsInPrison()
    val loyalty = readLoyalty()
    val strength = readIntInRange(
        "Fyzická síla ($MIN_STRENGTH-$MAX_STRENGTH): ",
        MIN_STRENGTH,
        MAX_STRENGTH
    )
    val iq = readIntInRange(
        "IQ ($MIN_IQ-$MAX_IQ): ",
        MIN_IQ,
        MAX_IQ
    )
    val updated = registry.updateDealer(
        dealer.id,
        nickname,
        yearsInPrison,
        loyalty,
        strength,
        iq
    )
    if (updated) {
        println("Dealer byl úspěšně upraven.")
    } else {
        println("Úprava selhala.")
    }
}
fun handleRemoveDealer(registry: DealerRegistry) {
    val dealer = findExistingDealer(registry) ?: return
    registry.removeDealer(dealer.id)
    println()
    println("Dealer ${dealer.nickname} byl vyhozen z organizace.")
}

fun handleDealersCount(registry: DealerRegistry) {
    println()
    println("Počet dealerů v organizaci: ${registry.dealerCount()}")
}

fun handleListDealers(registry: DealerRegistry) {
    val dealers = registry.allDealers()
    if (dealers.isEmpty()) {
        println("V evidenci není žádný dealer.")
    } else {
        println()
        println("===== SEZNAM DEALERŮ =====")
        printDealers(dealers)
    }
}
fun handleSearchDealersByNickname(registry: DealerRegistry) {
    val nickname = readNonBlankText("Zadej přezdívku: ")
    val dealers = registry.findByNickname(nickname)
    if (dealers.isEmpty()) {
        println()
        println("Žádný dealer s přezdívkou '$nickname' nebyl nalezen.")
    } else {
        println()
        println("===== NALEZENÍ DEALEŘI =====")
        printDealers(dealers)
    }
}
fun handleDealersByLoyalty(registry: DealerRegistry) {
    val loyalty = readLoyalty()
    val dealers = registry.dealersByLoyalty(loyalty)
    if (dealers.isEmpty()) {
        println()
        println("V evidenci není žádný dealer s touto loyalty.")
        return
    }
    println()
    println("===== SEZNAM DEALERŮ $loyalty =====")
    printDealers(dealers)
}
fun handleDealersWithMinIq(registry: DealerRegistry) {
    val minIq = readIntInRange(
        "IQ ($MIN_IQ-$MAX_IQ): ",
        MIN_IQ,
        MAX_IQ
    )
    val dealersWithMinIq = registry.dealersWithMinIq(minIq)
    if (dealersWithMinIq.isEmpty()) {
        println()
        println("V evidenci není žádný dealer s IQ alespoň $minIq.")
        return
    }
    println()
    println("===== DEALEŘI S IQ ALESPOŇ $minIq =====")
    printDealers(dealersWithMinIq)
}
fun handleDealersWithIqInRange(registry: DealerRegistry) {
    val minIq = readIntInRange(
        "Min IQ ($MIN_IQ-$MAX_IQ): ",
        MIN_IQ,
        MAX_IQ
    )
    val maxIq = readIntInRange(
        "Max IQ ($MIN_IQ-$MAX_IQ): ",
        MIN_IQ,
        MAX_IQ
    )
    if (maxIq < minIq) {
        println()
        println("Chyba: Maximální IQ musí být větší nebo rovno minimálnímu IQ.")
        return
    }
    val dealers = registry.dealersWithIqInRange(minIq, maxIq)
    if (dealers.isEmpty()) {
        println()
        println("V evidenci není žádný dealer s IQ v rozmezí ($minIq - $maxIq).")
        return
    }
    println()
    println("===== DEALEŘI S IQ V ROZEMEZÍ $minIq A $maxIq =====")
    printDealers(dealers)
}
fun handleDealersWithMinStrength(registry: DealerRegistry) {
    val minStrength = readIntInRange(
        "Min Fyzická síla ($MIN_STRENGTH-$MAX_STRENGTH): ",
        MIN_STRENGTH,
        MAX_STRENGTH
    )
    val dealers = registry.dealersWithMinStrength(minStrength)
    if (dealers.isEmpty()) {
        println()
        println("V evidenci není žádný dealer se sílou alespoň $minStrength.")
        return
    }
    println()
    println("===== DEALEŘI SE SÍLOU ALESPOŇ $minStrength =====")
    printDealers(dealers)
}
fun handleDealersByTextInNickname(registry: DealerRegistry) {
    val text = readNonBlankText("Zadej text: ")
    val dealers = registry.dealersByTextInNickname(text)
    if (dealers.isEmpty()) {
        println()
        println("Žádný dealer jehož přezdívka obsahuje '$text' nebyl nalezen.")
    } else {
        println()
        println("===== NALEZENÍ DEALEŘI =====")
        printDealers(dealers)
    }
}
fun handleEliteDealers(registry: DealerRegistry) {
    val dealers = registry.eliteDealers()
    if (dealers.isEmpty()) {
        println()
        println("Žádný dealer nesplňuje podmínky pro elite status.")
        return
    }
    println()
    println("===== SEZNAM ELITE DEALERŮ =====")
    printDealers(dealers)
}

fun handleSearchDealerById(registry: DealerRegistry) {
    val dealer = findExistingDealer(registry) ?: return
    println()
    println("===== NALEZENÝ DEALER =====")
    printDealer(dealer)
}
fun handleStrongestDealer(registry: DealerRegistry) {
    val dealer = registry.strongestDealer()
    if (dealer == null) {
        println()
        println("V evidenci není žádný dealer.")
        return
    }
    println()
    println("===== NEJSILĚJŠÍ DEALER =====")
    printDealer(dealer)
}
fun handleSmartestDealer(registry: DealerRegistry) {
    val dealer = registry.smartestDealer()
    if (dealer == null) {
        println()
        println("V evidenci není žádný dealer.")
        return
    }
    println()
    println("===== NEJCHYTŘEJŠÍ DEALER =====")
    printDealer(dealer)
}
fun handleDealerWithMostYearsInPrison(registry: DealerRegistry) {
    val dealer = registry.dealerWithMostYearsInPrison()
    if (dealer == null) {
        println()
        println("V evidenci není žádný dealer.")
        return
    }
    println()
    println("===== DEALER CO NEJVÍC SEDĚL =====")
    printDealer(dealer)
}
fun handleSmartestDealerInLoyalty(registry: DealerRegistry) {
    val loyalty = readLoyalty()
    val dealer = registry.smartestDealerFromLoyalty(loyalty)
    if (dealer == null) {
        println()
        println("V evidenci není žádný dealer s touto loyalty.")
        return
    }
    println()
    println("===== NEJCHYTŘEJŠÍ DEALER Z $loyalty =====")
    printDealer(dealer)
}
fun handleDealerOfTheYear(registry: DealerRegistry) {
    val dealer = registry.dealerOfTheYear()
    if (dealer == null) {
        println()
        println("V evidenci není žádný dealer.")
        return
    }
    println()
    println("===== NEJLEPŠÍ DEALER =====")
    printDealer(dealer)
}

fun handleAverageIq(registry: DealerRegistry) {
    val average = registry.averageIq()
    if (average == null) {
        println()
        println("V evidenci není žádný dealer.")
        return
    }
    println()
    println("Průměrné IQ dealerů: $average")
}
fun handleAverageStrength(registry: DealerRegistry) {
    val average = registry.averageStrength()
    if (average == null) {
        println()
        println("V evidenci není žádný dealer.")
        return
    }
    println()
    println("Průměrná síla dealerů: $average")
}

fun main() {
    val registry = InMemoryDealerRegistry()

    registry.addDealer("Rychlá Rota", 2, Loyalty.HOLDS_THE_LINE, 74, 96)
    registry.addDealer("Profesor", 12, Loyalty.INNER_CIRCLE, 31, 157)
    registry.addDealer("PROFESOR", 2, Loyalty.INNER_CIRCLE, 66, 120)
    registry.addDealer("debil", 0, Loyalty.RAT, 55, 55)
    registry.addDealer("debilek", 0, Loyalty.RAT, 40, 50)
    registry.addDealer("debilek123", 0, Loyalty.RAT, 40, 50)

    while (true) {
        showMenu()
        val choice = readln().toIntOrNull() ?: -1
        when (choice) {
            1 -> handleAddDealer(registry)
            2 -> handleEditDealer(registry)
            3 -> handleRemoveDealer(registry)
            4 -> handleDealersCount(registry)
            5 -> handleListDealers(registry)
            6 -> handleSearchDealersByNickname(registry)
            7 -> handleDealersByLoyalty(registry)
            8 -> handleDealersWithMinIq(registry)
            9 -> handleDealersWithIqInRange(registry)
            10 -> handleDealersWithMinStrength(registry)
            11 -> handleDealersByTextInNickname(registry)
            12 -> handleEliteDealers(registry)
            13 -> handleStrongestDealer(registry)
            14 -> handleSmartestDealer(registry)
            15 -> handleDealerWithMostYearsInPrison(registry)
            16 -> handleSmartestDealerInLoyalty(registry)
            17 -> handleDealerOfTheYear(registry)
            18 -> handleSearchDealerById(registry)
            19 -> handleAverageIq(registry)
            20 -> handleAverageStrength(registry)
            0 -> {
                showGoodbyeMessage()
                return
            }
            else -> println("Neplatná volba.")
        }
    }
}