package event

import (
	"github.com/james-wilder/scifi/universe"
	"github.com/james-wilder/scifi/util"
)

type Type int32

const (
	UniverseStart Type = iota
	LifeformStart
	LifeformDiscoversLifeform
	Colonization
	AsteroidPopulationCollapse
	EnvironmentalPopulationCollapse
	WarPopulationCollapse
	UniverseEnd

	techGrowthRate = 0.01
)

type Event struct {
	Type      Type
	At        universe.UniversalTime
	Lifeform  *universe.Lifeform
	Lifeform2 *universe.Lifeform
	Star      *universe.Star
	Distance  float64
}

func GetNextEvent(u *universe.Universe, now universe.UniversalTime) Event {
	e := Event{
		At:   universe.LifespanOfUniverse,
		Type: UniverseEnd,
	}

	// LifeformDiscoversLifeform
	for _, lifeform := range u.Lifeforms {
		for _, population := range lifeform.Populations {
			for _, lifeform2 := range u.Lifeforms {
				if lifeform2 == lifeform {
					continue
				}
				if util.SliceContains(lifeform2.Name, lifeform.Discovered) {
					continue
				}
				for _, population2 := range lifeform2.Populations {
					lifeformInformationLevel := float64(lifeform.Information) +
						float64(now-lifeform.StartYear)*techGrowthRate

					distance := universe.GetDistance(population.Location, population2.Location)

					// TODO: what to do with the arbitrary constant?
					techLevelRequiredToDetect := distance * 100000

					// detected when: detection > distance
					timeToDetect := (techLevelRequiredToDetect - lifeformInformationLevel) / techGrowthRate
					utimeToDetect := universe.UniversalTime(timeToDetect)

					if utimeToDetect < 0 {
						utimeToDetect = 0
					}

					if now+utimeToDetect < e.At {
						e = Event{
							Type:      LifeformDiscoversLifeform,
							At:        now + utimeToDetect,
							Lifeform:  lifeform,
							Lifeform2: lifeform2,
							Distance:  distance,
						}
					}
				}
			}
		}
	}

	// LifeformStart
	for _, star := range u.Stars {
		pop := u.GetPopulations(star)
		if len(pop) != 0 {
			continue
		}
		if star.LifeformWillEvolveAt < e.At {
			e = Event{
				At:   star.LifeformWillEvolveAt,
				Type: LifeformStart,
				Star: star,
			}
		}
	}

	return e
}
