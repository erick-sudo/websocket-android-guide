LazyColumn {
items(messages) { hiveMessage ->
Row(
) {
if(hiveMessage.userId == 1) Box(modifier = Modifier.weight(0.15f))
Column(
modifier = Modifier
.weight(0.85f)
.padding(5.dp),
horizontalAlignment = when(hiveMessage.userId == 1) {
true -> Alignment.End
else -> Alignment.Start
}
) {
Card(
modifier = Modifier,
colors = when(hiveMessage.userId == 1) {
true -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
else -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
}
) {
Text(
text = hiveMessage.text,
modifier = Modifier
.padding(start = 10.dp, end = 10.dp, top = 15.dp)
)
Text(
text = SimpleDateFormat.getInstance().format(hiveMessage.createdAt),
fontSize = 10.sp,
fontWeight = FontWeight.Bold,
modifier = Modifier
.padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
)
}
}
if(hiveMessage.userId != 1) Box(modifier = Modifier.weight(0.15f))
}
}
}