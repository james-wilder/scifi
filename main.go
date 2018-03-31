package main

import (
	"fmt"
	"scifi/event"
	"scifi/names"
	"scifi/tech"
	"scifi/universe"
)

func main() {
	var myUniverse universe.Universe

	fmt.Printf("Building universe\n")

	names.Init()
	tech.Init()

	myUniverse.GenerateStars(20)

	fmt.Printf("Universe contains %d stars\n", len(myUniverse.Stars))
	fmt.Printf("Universe contains %d lifeforms\n", len(myUniverse.Lifeforms))

	var now = universe.HumanStartTime
	e := event.Event{
		At:   universe.UniversalTime(0),
		Type: event.UniverseStart,
	}

	for {
		fmt.Println("Now:", now.String())

		e = event.GetNextEvent(&myUniverse, now)

		fmt.Printf("Next event at %s (%s years)\n", e.At.String(), (e.At - now).String())
		fmt.Println("Next event type", e.Type)

		now = e.At

		if e.Type == event.UniverseEnd {
			fmt.Println("Universe has ended at", now.String())
			break
		}

		// TODO: maybe move into event?
		switch e.Type {
		case event.LifeformDiscoversLifeform:
			fmt.Printf("%s found %s:\n", e.Lifeform.Name, e.Lifeform2.Name)
			e.Lifeform.Discovered = append(e.Lifeform.Discovered, e.Lifeform2.Name)
		case event.LifeformStart:
			lifeform := myUniverse.GenerateLifeform(e.Star, now)
			fmt.Printf("New lifeform %s:\n", lifeform.Name)
		}
	}
}
