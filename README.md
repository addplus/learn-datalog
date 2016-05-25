# Install

```
brew install boot-clj
boot -u
git clone https://github.com/addplus/learn-datalog.git
cd learn-datalog
boot dev
```

This starts an nREPL server and a client and loads the example `learn-datalog`
namespace.

# Usage

While you can type in the terminal at the prompt opened by `boot dev`,
for convenience I would recommend creating a Remote nREPL configuration in
IntelliJ with the "Use Leiningen REPL port" option.

Afterwards pressing `Cmd+R` will connect to the already running nREPL process.
Then just open the `learn_datalog.clj`, position into the middle of any of
the query expressions and press `Cmd+Shift+P` to send it to the REPL.

Enjoy, until the original site is back up again.


# Credits

The original [Learn Datalog Today](http://www.learndatalogtoday.org) is
[broken at the moment](https://github.com/jonase/learndatalogtoday/issues/28)
[@fasiha](https://github.com/fasiha) recommended to use a
[DataScript](https://gist.github.com/fasiha/2ab2c1cb203c26a2b63532831f1b6021#file-learn_datalog_today-clj)
environment to allow running the example queries.
This repo just packages up that code.

Thanks, [@fasiha](https://github.com/fasiha)!
