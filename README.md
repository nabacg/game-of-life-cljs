
# Game of Life 

ClojureScript and Reagent built page visualising steps in [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)

## Demo
<img src="https://github.com/nabacg/game-of-life-cljs/blob/master/images/demo-ld.gif?raw=true" width="240" height="360" />

### Development mode
To start the Figwheel compiler, navigate to the project folder and run the following command in the terminal:

```
lein figwheel
```

Figwheel will automatically push cljs changes to the browser.
Once Figwheel starts up, you should be able to open the `public/index.html` page in the browser.

### REPL

The project is setup to start nREPL on port `7002` once Figwheel starts.
Once you connect to the nREPL, run `(cljs)` to switch to the ClojureScript REPL.

### Building for production

```
lein clean
lein package
```
