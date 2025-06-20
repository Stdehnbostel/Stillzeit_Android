//
//  ContentView.swift
//  Stillzeit
//
//  Created by Stefan on 26.02.25.
//

import SwiftUI

struct ContentView: View {
    
    @State private var comment : String = ""
    
    let timeStampModel = TimeStamps()
    
    var body: some View {
        NavigationStack {
            VStack {
                VStack {
                    NavigationLink(destination: ProtocolView(model: ProtocolModel(dbManager: timeStampModel.dbManager) )) {
                        StillzeitButton(label: "Protokoll anzeigen")
                    }
                    NavigationLink(
                        destination: LastFeedingView(lastFeeding: timeStampModel.dbManager.getAllFeedingTimes().last, lastFeedingTimeString: timeStampModel.generateDurationString(timeStampModel.rawDuration()))
                    ) {
                        StillzeitButton(label: "letztes Stillen")
                    }
                    
                }.padding()
                    .navigationTitle("Verlauf anzeigen")
                Text("Neue Stillmahlzeit aufnehmen:").font(.title)
                Text("Mit bestehenden Daten").font(.headline)
                NavigationLink(
                    destination: AddFeedingView(dbManager: timeStampModel.dbManager, addFeedingModel: AddFeedingModel(dbManager: timeStampModel.dbManager))
                ) {
                    StillzeitButton(label: "Daten eingeben")
                }.padding()
                Text("Daten aufzeichnen:").font(.headline)
                VStack {
                    Text(timeStampModel.feedingStartText).frame(maxHeight: .infinity)
                    Text(timeStampModel.feedingEndText)
                }.padding().frame(maxHeight: .infinity)
                
                TimerView(time_stamps: timeStampModel).padding().frame(maxHeight: .infinity)
                VStack {
                    Text("Kommentar:")
                    TextField("", text: $comment)
                }
                .frame(maxHeight: .infinity).textFieldStyle(.roundedBorder)
                Button(action: {
                    timeStampModel.processTimestampButton(comment: comment)
                    if timeStampModel.feeding { comment = "" }
                }
                ){
                    Text("Zeitstempel aufnehmen").scaledToFill()
                        .frame(maxWidth: .infinity, alignment: .bottom).padding().background(Color.purple).foregroundColor(.white)
                }
                .clipShape(Capsule()).padding()
            }
        }
    }
}

#Preview {
    ContentView()
}
