package hmm

import (
	"math/rand"
	"strings"
)

type HmmModel struct {
	rand              *rand.Rand
	randomSource      rand.Source
	firstLetterCounts map[string]int
	twoLetterTotals   map[string]map[string]int
	threeLetterTotals map[string]map[string]int
}

func CreateHmmModel(source []string) *HmmModel {
	var model HmmModel

	model.randomSource = rand.NewSource(42)
	model.rand = rand.New(model.randomSource)
	model.rand.Seed(42)

	model.firstLetterCounts = make(map[string]int)
	model.twoLetterTotals = make(map[string]map[string]int)
	model.threeLetterTotals = make(map[string]map[string]int)
	for _, item := range source {
		item = strings.ToLower(item)
		var lastRune rune
		var runeCount int
		var runeBeforelast rune
		for _, rune := range item {
			if runeCount == 0 {
				// first rune
				model.firstLetterCounts[string(rune)]++
			} else if runeCount == 1 {
				// second rune
				key := string(lastRune)
				_, ok := model.twoLetterTotals[key]
				if !ok {
					model.twoLetterTotals[key] = make(map[string]int)
				}
				model.twoLetterTotals[key][string(rune)]++
				runeBeforelast = lastRune
			} else {
				// later runes
				key := string(runeBeforelast) + string(lastRune)
				_, ok := model.threeLetterTotals[key]
				if !ok {
					model.threeLetterTotals[key] = make(map[string]int)
				}
				model.threeLetterTotals[key][string(rune)]++
				runeBeforelast = lastRune
			}
			lastRune = rune
			runeCount++
		}
	}
	return &model
}

func (model *HmmModel) Generate() string {
	for {
		generated := model.generateAttempt()

		if len(generated) < 5 || len(generated) > 12 {
			continue
		}

		if countConsecutiveIdenticalRunes(generated) > 2 {
			continue
		}

		return generated
	}
}

func countConsecutiveIdenticalRunes(s string) int {
	count := 0
	var lastR rune
	for i, r := range s {
		if i == 0 || r == lastR {
			count++
		} else {
			lastR = r
			count = 1
		}
	}
	return count
}

func (model *HmmModel) generateAttempt() string {
	generated := ""
	lastLetter := ""

	newLetter := model.pickOne(model.firstLetterCounts)
	generated = generated + strings.ToUpper(newLetter)
	letterBeforeLast := newLetter

	newLetter = model.pickOne(model.twoLetterTotals[letterBeforeLast])
	generated = generated + newLetter
	lastLetter = newLetter

	for lastLetter != "." {
		newLetter = model.pickOne(model.threeLetterTotals[letterBeforeLast+lastLetter])
		if newLetter != "." {
			if lastLetter == " " || lastLetter == "'" {
				generated = generated + strings.ToUpper(newLetter)
			} else {
				generated = generated + newLetter
			}
		}
		letterBeforeLast = lastLetter
		lastLetter = newLetter
	}
	return generated
}

func (model *HmmModel) pickOne(counts map[string]int) string {
	total := 0
	var keys []string
	for key := range counts {
		keys = append(keys, key)
		total += counts[key]
	}

	pick := model.rand.Intn(total)
	runningTotal := 0
	for _, key := range keys {
		runningTotal = runningTotal + counts[key]
		if runningTotal >= pick {
			return key
		}
	}
	panic("arg!")
}
