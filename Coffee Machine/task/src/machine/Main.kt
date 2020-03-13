package machine

import java.util.*

class CoffeeMachine
    (
        private var water: Int,
        private var milk: Int,
        private var beans: Int,
        private var cups: Int,
        private var money: Int
    ) {

    private var state = MachineState.OFF
        set(value) {
            println( when (value) {
                MachineState.MAIN -> '\n' + "Write action (buy, fill, take, remaining, exit): "
                MachineState.COFFEE_CHOICE -> "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: "
                MachineState.WAIT_FOR_WATER -> "Write how many ml of water do you want to add: "
                MachineState.WAIT_FOR_MILK -> "Write how many ml of milk do you want to add: "
                MachineState.WAIT_FOR_BEANS -> "Write how many grams of coffee beans do you want to add: "
                MachineState.WAIT_FOR_CUPS -> "Write how many disposable cups of coffee do you want to add: "
                MachineState.OFF -> ""
            })
            field = value
        }

    private enum class MachineState {
        OFF, MAIN, COFFEE_CHOICE, WAIT_FOR_WATER, WAIT_FOR_MILK, WAIT_FOR_BEANS, WAIT_FOR_CUPS
    }

    private enum class CoffeeType(val water: Int, val milk: Int, val beans: Int, val money: Int) {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6);
    }

    fun action(string: String): Boolean {
        when (state) {

            MachineState.MAIN -> when (string) {
                "remaining" -> showStats()
                "buy" -> state = MachineState.COFFEE_CHOICE
                "fill" -> state = MachineState.WAIT_FOR_WATER
                "take"-> {
                    println("I gave you \$$money")
                    money = 0
                    state = MachineState.MAIN
                }
                "exit" -> {
                    state = MachineState.OFF
                    return false
                }
            }

            MachineState.COFFEE_CHOICE -> when (string) {
                "1" -> makeCoffee(CoffeeType.ESPRESSO)
                "2" -> makeCoffee(CoffeeType.LATTE)
                "3" -> makeCoffee(CoffeeType.CAPPUCCINO)
                "back" -> state = MachineState.MAIN
            }

            MachineState.WAIT_FOR_WATER -> {
                water += string.toInt()
                state = MachineState.WAIT_FOR_MILK
            }
            MachineState.WAIT_FOR_MILK -> {
                milk += string.toInt()
                state = MachineState.WAIT_FOR_BEANS
            }
            MachineState.WAIT_FOR_BEANS -> {
                beans += string.toInt()
                state = MachineState.WAIT_FOR_CUPS
            }
            MachineState.WAIT_FOR_CUPS -> {
                cups += string.toInt()
                state = MachineState.MAIN
            }

            else -> if (string == "on") state = MachineState.MAIN else return false

        }
        return true
    }

    private fun makeCoffee(coffeeType: CoffeeType) {
        when {
            water < coffeeType.water -> {
                println("Sorry, not enough water!")
            }
            milk < coffeeType.milk -> {
                println("Sorry, not enough milk!")
            }
            beans < coffeeType.beans -> {
                println("Sorry, not enough coffee beans!")
            }
            cups < 1 -> {
                println("Sorry, not enough cups!")
            }
            else -> {
                println("I have enough resources, making you a coffee!")
                water -= coffeeType.water
                milk -= coffeeType.milk
                beans -= coffeeType.beans
                cups -= 1
                money += coffeeType.money
            }
        }
        state = MachineState.MAIN
    }

    private fun showStats() {
        println("The coffee machine has:")
        println("$water of water")
        println("$milk of milk")
        println("$beans of coffee beans")
        println("$cups of disposable cups")
        println("\$$money of money")
        state = MachineState.MAIN
    }

}

fun main() {
    val scanner = Scanner(System.`in`)
    val coffeeMachine = CoffeeMachine(400, 540, 120, 9, 550)
    coffeeMachine.action("on")
    while (coffeeMachine.action(scanner.nextLine()));
}
