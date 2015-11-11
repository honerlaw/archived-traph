


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

