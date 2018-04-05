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

	// 20 tiny
	// 100 medium
	// 200 large
	// 1000 extra large
	myUniverse.GenerateStars(50)

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
		case event.Colonization:
			myUniverse.Colonize(e.Star, now, e.Lifeform)
			fmt.Printf("%s has colonized new star system (now has %d populations)\n", e.Lifeform.Name, len(e.Lifeform.Populations))
		}
	}
}
