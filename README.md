


Entity Updating

So the player has update segments
Each segment consists of one or more blocks
These blocks are added to each segment based on flags

So entities have update segments

A segment writes bits (basically we always need to write these bits)

A segment can contain blocks

Blocks write data (bits or bytes) and are optional

Blocks have flags associated with them

Flags may have data associated with them as well

Known Segments (Player)

- State (8 blocks)
-- Blocks
--- graphics
--- animation
--- chat
--- face entity
--- appearance
--- face coordinates
--- primary hit
--- secondary hit
- Movement (2 blocks)
-- Blocks
--- placement (teleport / etc)
--- movement (stand, walk, run)
- Add Player (no blocks)

Handle Incoming Packets

- First Come / First Serve
- Basically we put actions in a global queue
-- The first one in the queue is the first one to have their action executed
-- The players won't know this because the actual update cycle takes place every 600ms
-- So basically we don't loop through each player and update them. When a packet comes in
-- we place an action into the queue of actions to execute. The action is then executed however it needs to be executed
-- If the action has an initial delay we skip it in the queue and move to the next action
-- Once the initial delay has passed we remove it
-- If it has a post delay then we do the same thing and skip it and move to the next action
-- Once the post delay has passed we remove it
-- Only one action can exist in the queue at a time (so if you spam click but already have a non cancelable action it just ignores it).
-- If you have a cancelable action then we can immediately disregard it and execute the next action (insert it into the queue).
-- Every 600ms we cycle through the queue and remove / execute the actions

