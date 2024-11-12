package com.example.ecommerspoo

data class Products(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    var quantityInStock: Int
) {
    fun displayDetails(): String {
        return "$name - $price MAD"
    }

    fun addQuantityInStock(quantity: Int) {
        quantityInStock += quantity
    }

    fun removeFromStock(quantity: Int): Boolean {
        return if (quantity <= quantityInStock) {
            quantityInStock -= quantity
            true
        } else {
            false
        }
    }
}

data class Client(val id: Int, val name: String, val email: String, val panier: MutableList<Pair<Products, Int>> = mutableListOf()) {
    fun addProductInPanier(product: Products, quantity: Int): Boolean {
        if (quantity > product.quantityInStock) {
            println("The quantity is too large; not enough in stock.")
            return false
        }

        val cartProduct = panier.find { it.first.id == product.id }

        if (cartProduct == null) {
            panier.add(Pair(product.copy(quantityInStock = product.quantityInStock - quantity), quantity))
            println("${product.name} ($quantity) has been added to the cart.")
            return true
        } else {
            println("${product.name} is already in the cart.")
            return false
        }
    }

    fun removeProduct(product: Products, quantity: Int) {
        val cartProduct = panier.find { it.first.id == product.id }

        if (cartProduct != null) {
            val currentQuantityInCart = cartProduct.second

            if (quantity > currentQuantityInCart) {
                println("Cannot remove more than what is in the cart. Current quantity: $currentQuantityInCart")
                return
            }

            if (currentQuantityInCart == quantity) {
                panier.remove(cartProduct)
            } else {
                panier[panier.indexOf(cartProduct)] = Pair(cartProduct.first, currentQuantityInCart - quantity)
            }

            val updatedProduct = product.copy(quantityInStock = product.quantityInStock + quantity)
            println("${product.name} ($quantity) removed successfully. Stock updated to ${updatedProduct.quantityInStock}.")
        } else {
            println("${product.name} is not in the cart.")
        }
    }

    fun displayPanier() {
        if (panier.isEmpty()) {
            println("The cart is empty.")
        } else {
            for (pair in panier) {
                val product = pair.first
                val quantity = pair.second
                println("Name: ${product.name}, Description: ${product.description}, Price: ${product.price}, Quantity: $quantity")
            }
        }
    }

    fun calculateTotalPanier(): Double {
        return panier.sumOf { it.first.price * it.second }
    }
}
data class Commend(val id:Int, val client: Client, val product: MutableList<Pair<Products, Int>>, var statu:String){

    fun calculerTotal():Double{
        return product.sumOf { it.first.price * it.second  }
    }

    fun changerStatut(NewStatus:String){
        val validStatuses = listOf("PENDING", "SHIPPED", "DELIVERED")

        if(NewStatus in validStatuses){
            statu = NewStatus
            println("Order status changed to: $statu")
        }else {
            println("Invalid status: $NewStatus. Status not changed.")
        }

    }

}
data class Boutique(val catalogue:MutableList<Products>, val commend: MutableList<Commend>){

    fun addProductInCatalogue(product: Products){
        catalogue.add(product)

    }

    fun dispalycatalogue(){
        return if (catalogue.isEmpty()){
            println("catalogue is emlty")
        }else{
            for(product in catalogue){
                println("Name: ${product.name}, Description: ${product.description}, Price: ${product.price}, Quantity: ${product.quantityInStock}")
            }
        }
    }


    fun createCommand(client: Client):Boolean{

         if(client.panier.isEmpty()){
             println("Cannot create command. The client's cart is empty.")
             return false
         }else{
             val newCommand = Commend(
                 id = generateUniqueId(),
                 client = client,
                 product = client.panier.toMutableList(),
                 statu = "PENDING"
             )

             commend.add(newCommand)
             println("Command created successfully with ID: ${newCommand.id}")
             return true

         }

    }
    private var currentId = 0
    fun generateUniqueId(): Int {
        currentId++
        return currentId

    }
    fun desplayClientCommand(client: Client){
        val clientCommands = commend.filter { it.client.id == client.id }

        if(clientCommands.isEmpty()){
            println("No commands found for client: ${client.name}.")

        }else{
            for(command in clientCommands){
                println("Command ID: ${command.id}")
                println("Status: ${command.statu}")
                println("Products in Command:")

                command.product.forEach { pair ->
                    val product = pair.first
                    val quantity = pair.second
                    println("- ${product.name}, Price: ${product.price} MAD, Quantity: $quantity")
                }
                val total = command.calculerTotal()
                println("Total Price: $total MAD")
                println("-------------")
            }


        }

    }
}














fun main() {

    val product1 = Products(id = 1, name = "Gaming Laptop", description = "High-performance gaming laptop.", price = 1500.0, quantityInStock = 16)
    val product2 = Products(id = 2, name = "MacBook Pro", description = "Apple MacBook Pro with M1 chip.", price = 2500.0, quantityInStock = 15)


    val client = Client(id = 1, name = "yassine", email = "yassine@example.com")

    client.addProductInPanier(product1, 2)
    client.addProductInPanier(product2, 5)
    client.addProductInPanier(product1, 11)
    client.displayPanier()

    println("\nCalculating total price")
    val total = client.calculateTotalPanier()
    println("Total price in the cart: $total MAD")


    val boutique = Boutique(mutableListOf(product1, product2), mutableListOf())
    println("\n Creating a command")
    boutique.createCommand(client)
    boutique.desplayClientCommand(client)


    println("\n Changing command status")
    val command = boutique.commend.first()
    command.changerStatut("SHIPPED")
    command.changerStatut("CANCELLED")

    client.removeProduct(product1, 1)
    client.removeProduct(product2, 3)

    client.displayPanier()
    val newTotal = client.calculateTotalPanier()
    println("New total price in the cart: $newTotal MAD")

    client.removeProduct(product1, 3)
}
