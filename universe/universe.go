package universe

import (
	"fmt"
	"math"
	"math/rand"

	"github.com/james-wilder/scifi/names"
)

type UniversalTime float64
type Coord float32
type LogTechLevel float32

const (
	HumanStartTime            UniversalTime = 13800000000
	EarliestLifeformStartTime UniversalTime = 5000000000
	LifespanOfUniverse        UniversalTime = 100000000000
)

type Universe struct {
	Stars     []*Star
	Lifeforms []*Lifeform
}

type Star struct {
	X                    Coord
	Y                    Coord
	Z                    Coord
	LifeformWillEvolveAt UniversalTime
}

type Population struct {
	Location *Star // TODO: other locations, eg stations, ships, habitats
	Size     int64
}

type Lifeform struct {
	Name         string
	Populations  []*Population
	Energy       LogTechLevel
	Information  LogTechLevel
	Intelligence LogTechLevel
	Material     LogTechLevel
	Social       LogTechLevel
	Transport    LogTechLevel
	Discovered   []string
	StartYear    UniversalTime
}

func (t UniversalTime) String() string {
	return fmt.Sprintf("%f", float64(t))
}

func GetDistance(location *Star, location2 *Star) float64 {
	xd := location.X - location2.X
	yd := location.Y - location2.Y
	zd := location.Z - location2.Z
	return math.Sqrt(float64(xd*xd + yd*yd + zd*zd))
}

func (u *Universe) GenerateStars(n int) {
	for i := 0; i < n; i++ {
		x := randomCoord()
		y := randomCoord()
		z := randomCoord()
		star := Star{
			X:                    x,
			Y:                    y,
			Z:                    z,
			LifeformWillEvolveAt: getRandomLifeformStartTime(),
		}
		u.Stars = append(u.Stars, &star)
	}
}

func getRandomLifeformStartTime() UniversalTime {
	var time UniversalTime
	// TODO: latest time?
	for time < EarliestLifeformStartTime {
		norm := rand.NormFloat64()
		time = UniversalTime(norm*1000000000) + HumanStartTime
	}
	return time
}

func randomCoord() Coord {
	return Coord(100*rand.Float32() - 0.5)
}

func (u *Universe) GenerateLifeform(star *Star, now UniversalTime) *Lifeform {
	name := names.CreateName()
	lifeform := Lifeform{
		Name:         name,
		Energy:       getRandomKardshevStartLevel(),
		Information:  getRandomKardshevStartLevel(),
		Intelligence: getRandomKardshevStartLevel(),
		Material:     getRandomKardshevStartLevel(),
		Social:       getRandomKardshevStartLevel(),
		Transport:    getRandomKardshevStartLevel(),
		Populations: []*Population{
			{
				Location: star,
			},
		},
		StartYear: now,
	}
	u.Lifeforms = append(u.Lifeforms, &lifeform)
	return &lifeform
}

func (u *Universe) RandomStar() *Star {
	return u.Stars[rand.Intn(len(u.Stars))]
}

func getRandomKardshevStartLevel() LogTechLevel {
	return LogTechLevel(1 + rand.Float32()/10) // 1 - 1.1
}

func (u *Universe) GetPopulations(star *Star) []*Population {
	var populations []*Population
	for _, lifeform := range u.Lifeforms {
		for _, population := range lifeform.Populations {
			if population.Location == star {
				populations = append(populations, population)
			}
		}
	}
	return populations
}
