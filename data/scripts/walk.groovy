

// receive the packet and check what it is

int size = packet.getSize();
if(packet.getOpcode() == 248) {
	size -= 14;
}

int steps = (size - 5) / 2;
int[][] path = new int[steps][2];


int firstStepX = packet.getPayload().getLEShortA;
for(int i = 0; i < steps; ++i) {
	path[i][0] = packet.getPayload().get();
	path[i][1] = packet.getPayload().get();
}
int firstStepY = packet.getPayload().getLEShort();


def queue = client.getGameData().getPlayer().getPathQueue();

queue.reset().add(new Location(firstStepX, firstStepY));

for(int i = 0; i < steps; ++i) {
	path[i][0] += firstStepX;
	path[i][1] += firstStepY;
	queue.add(new Location(path[i][0], path[i][1]);
}


// define the action :)
client.getActionManager().add(new ActionHandler(ActionType.WALK, data -> {

	// process the queue
	def queue = data.getClient().getGameData().getPlayer().getPathQueue();
	
	def walkPoint = queue.poll();
	def runPoint = null;
	if(queue.isRun()) {
		runPoint = queue.poll();
	}
	
	def primary = -1;
	def secondary = -1;
	
	if(walkPoint != null && walkPoint.getDirection() != -1) {
		primary = 
	}
	
	if(runPoint != null && runPoint.getDirection() != -1) {
		
	}
	
	

	return null;
}));


def packets() {
	return [248, 164, 98] as int[];
}