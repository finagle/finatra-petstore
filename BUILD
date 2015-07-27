maven_layout()

target(name='finatra-petstore',
  dependencies=[
    'finatra-petstore/src/main/scala'
  ]
)

target(name='tests',
  dependencies=[
    'finatra-petstore/src/test/scala'
  ]
)

jvm_binary(
  name='bin',
  basename='finatra-petstore',
  main='io.github.finagle.example.petstore.PetstoreServerMain',
  dependencies=[
    ':finatra-petstore'
  ],
  excludes=[
    exclude('org.slf4j', 'slf4j-jdk14'),
    exclude('log4j', 'log4j')
  ]
)
