calc:
	java -jar dist/ExpressionEvaluator.jar "$(filter-out $@,$(MAKECMDGOALS))"
%:
	@:

