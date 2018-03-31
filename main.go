package main

import (
	"fmt"

	"github.com/james-wilder/scifi/event"
	"github.com/james-wilder/scifi/names"
	"github.com/james-wilder/scifi/universe"
)

func main() {
	var myUniverse universe.Universe

	fmt.Printf("Building universe\n")

	names.Init()

	myUniverse.GenerateStars(20)

	fmt.Printf("Universe contains %d stars\n", len(myUniverse.Stars))

	var now = universe.UniversalTime(0)
	e := event.Event{
		At:   universe.UniversalTime(0),
		Type: event.UniverseStart,
	}
	fmt.Printf("At %s the universe begins\n", now.String())

	for {
		e = event.GetNextEvent(&myUniverse, now)

		now = e.At

		fmt.Printf("At %s years ", now.String())

		if e.Type == event.UniverseEnd {
			fmt.Println("Universe has ended at", now.String())
			break
		}

		// TODO: maybe move into event?
		switch e.Type {
		case event.LifeformDiscoversLifeform:
			fmt.Printf("%s found %s at %f light years\n", e.Lifeform.Name, e.Lifeform2.Name, e.Distance)
			e.Lifeform.Discovered = append(e.Lifeform.Discovered, e.Lifeform2.Name)
		case event.LifeformStart:
			lifeform := myUniverse.GenerateLifeform(e.Star, now)
			fmt.Printf("%s have become sentient (Universe contains %d lifeforms)\n", lifeform.Name, len(myUniverse.Lifeforms))
		}
	}
}
