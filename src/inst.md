change the logic of try-catch
delete CountDownLatch


Stocker end ->
1) Picker - full
2) Depot  - full

isInterrupted - change (while picker.Size() = 0 - false)
add to Depot the logics - if full write about it and if Depot.Size = 0 - print
await