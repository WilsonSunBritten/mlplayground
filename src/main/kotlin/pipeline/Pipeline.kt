package pipeline

open class Pipeline {
    fun setup(data: Data) {}

    fun process(data: Data): Data {
        // Do some form of process to transform the data
        return data
    }
}

interface Data