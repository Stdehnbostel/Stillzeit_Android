//
//  TimeStamps.swift
//  Stillzeit
//
//  Created by Stefan on 24.05.25.
//

import Foundation

@Observable
class TimeStamps {
    
    var feeding = false
    var feedingStart: Date = Date()
    var feedingEnd: Date = Date()
    
    var feedingStartText: String = "Beginn: "
    var feedingEndText: String = "Ende: "
    var feedingDurationText: String = "Dauer: "
    
    let dbManager = DatabaseConnection()
    
    
    
    func updateFeedingDuration(feedingDurationText: String, feeding: Bool) -> String {
        
        var durationString: String = feedingDurationText
        if feeding {
            let duration: DateComponents = rawDuration()
            durationString = generateDurationString(duration)
        }
        return durationString
    }

    func generateDurationString(_ duration: DateComponents) -> String {
        "Dauer: \(duration.hour ?? 0):\(duration.minute ?? 0):\(duration.second ?? 0)"
    }

    func rawDuration() -> DateComponents {
        Calendar.current.dateComponents([.hour, .minute, .second], from: feedingStart, to: Date())
    }

    func generateTimeString(_ date: Date) -> String {
        date.formatted(.dateTime.hour().minute().second())
    }
    
    func generateISOTimeString(_ date: Date) -> String {
        let formatter = ISO8601DateFormatter()
        return formatter.string(from: date)
    }
    
    func generateDateFromISOTimeString(_ isoTimeString: String) -> Date {
        let formatter = ISO8601DateFormatter()
        return formatter.date(from: isoTimeString)!
    }
    
    func generateTimestamp(comment: String = "") {
        if !feeding {
            feedingStart = Date()
            _ = dbManager.insertFeedingTime(id: dbManager.size, start: generateISOTimeString(feedingStart), end: "", duration: "", comment: "", active: true)
        } else {
            feedingEnd = Date()
            let duration = rawDuration()
            _ = dbManager.finishFeeding(id: dbManager.size - 1, end: generateISOTimeString(feedingEnd), duration: "\(duration.hour ?? 0 ):\(duration.minute ?? 0):\(duration.second ?? 0)", comment: comment, active: false)
        }
        self.feeding.toggle()
    }
    
    func processTimestampButton(comment: String) {
        if !feeding {
            generateTimestamp()
            feedingStartText = "Beginn: \( generateTimeString(feedingStart)) Uhr"
            feedingEndText = "Ende: "
        } else {
            generateTimestamp(comment: comment)
            feedingEndText = "Ende: \(generateTimeString(feedingEnd)) Uhr"
            feedingDurationText = generateDurationString(rawDuration())
        }
    }
}


