# Jervis (AgentGame 2012 winner)

[![fog of war video](media/output.gif)](https://vimeo.com/232976281)

Jervis is an agent made for the AgentGame 2012 competition that was organized by BME MIT. It finished first in all 7 rounds. This repository contains the final version that was submitted for the last round. All code for previous rounds can be found in the repo's history including the early AgentSpeak-only versions.

## Notable features
### Emergent behaviour during scavenging

Jervis agents try to locally minimize fog of war darkness during each step.
Fog of war is "darker" where more foods could have spawned since last seen.

This results in interesting and useful emergent behaviours, such as:
 - Agents look around when a new food has spawned on the map.
 - Agents turn away when they encounter an other friendly agent (and their sight overlaps).
 - Agents don't go too close to the edge of the map.
 - Agents can simply be denied entry to the size-limited "expensive" water tiles during scavenging, and they still do fine
 
I created an semi-transparent overlay for displaying fog of darkness and the emergent behaviours. This can be seen in the video.

### Java

Contains only a single line of Jason/AgentSpeak code for bootstrapping into Java code.
All perceptions are read by Java code, and all actions are issued by Java code.
Jervis agents communicate through shared memory.

### Other notable features

  - Visual map overlay for debugging and understanding purposes
  - Good heuristics for figuring out water locations with only a few steps into the water (water cannot be seen, but only felt)
  - A* based route planning when going for food
    - Obstacle avoidance
    - Water avoidance
  - Internal time that is aware of agent stepping order 
  - Simulates the expected results of its own actions, and can detect anomalies
  - Can detect and remove malicious beliefs broadcasted by enemy agents (too many of these would result in slowdown and a loss)
  - Can handle the case where enemy agents completly block a part of the map
  - Can revive agents that run out of energy
  
# License
  
Copyright (c) 2017 Kálmán Tarnay

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
