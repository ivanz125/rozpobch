package main

import (
	"math/rand"
	"fmt"
	"time"
)

func main() {
	warehouse := make(chan int)
	truckChannel := make(chan int)
	priceChannel := make(chan int)

	go generateItems(warehouse)
	go stealItems(warehouse, truckChannel)
	go putIntoTruck(truckChannel, priceChannel)
	go countPrice(priceChannel)

	fmt.Scanln()
}

func generateItems(warehouse chan int)  {
	for i := 0; i < 15 ; i++ {
		warehouse <- rand.Intn(100)
	}
	warehouse <- -1
}


func stealItems(warehouse chan int, truckChannel chan int) {
	for true {
		item := <- warehouse
		if item == -1 {
			truckChannel <- -1
			return
		}
		fmt.Println("Item stolen: ", item)
		truckChannel <- item
		timeWasted := time.Duration(rand.Intn(50))
		time.Sleep(time.Millisecond * timeWasted)
	}
}

func putIntoTruck(truckChannel chan int, priceChannel chan int) {
	for true {
		item := <- truckChannel
		if item == -1 {
			priceChannel <- -1
			return
		}
		fmt.Println("Item put into truck: ", item)
		timeWasted := time.Duration(rand.Intn(50))
		time.Sleep(time.Millisecond * timeWasted)
		priceChannel <- item
	}
}

func countPrice(priceChannel chan int) {
	total := 0
	for (true) {
		itemPrice := <- priceChannel
		if itemPrice == -1 {
			break
		}
		total += itemPrice
	}
	fmt.Println("Total items price: ", total)
}