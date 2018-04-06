package main

import (
	"math/rand"
	"fmt"
	"time"
)

const nA int = 10
const nB int = 100
var currentArea int
var forest [][]bool

func main() {
	makeForest()
	assignTasks()

	fmt.Scanln()
}

func makeForest() {
	forest = make([][]bool, nA)
	for i:= 0; i < nA; i++ {
		forest[i] = make([]bool, nB)
	}
	rand.Seed(time.Now().UTC().UnixNano())
	posA := rand.Intn(10)
	posB := rand.Intn(100)
	forest[posA][posB] = true
}

func searchWinnie(areaIndex int, area []bool) {
	for i := 0; i < len(area); i++ {
		time.Sleep(time.Duration(rand.Intn(10)))
		//time.Sleep(10)
		if area[i] == true {
			fmt.Println("Bear was in area number ", (areaIndex + 1), ", he was successfully found and punished")
			return
		}
	}
}

func getTaskFromPool() []bool {
	if currentArea >= 10 {
		return nil
	}
	currentArea++
	return forest[currentArea - 1]
}

func assignTasks() {
	for i := 0; i <= nA; i++ {
		go searchWinnie(i, getTaskFromPool())
	}
}