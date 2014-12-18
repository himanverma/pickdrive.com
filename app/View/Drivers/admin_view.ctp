<div class="drivers view">
<h2><?php echo __('Driver'); ?></h2>
	<dl>
		<dt><?php echo __('Id'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['id']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Full Name'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['full_name']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('DeviceId'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['deviceId']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Email'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['email']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Contact Number'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['contact_number']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Current Lat'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['current_lat']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Current Lng'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['current_lng']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Available'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['available']); ?>
			&nbsp;
		</dd>
		<dt><?php echo __('Photo Path'); ?></dt>
		<dd>
			<?php echo h($driver['Driver']['photo_path']); ?>
			&nbsp;
		</dd>
	</dl>
</div>
<div class="actions">
	<h3><?php echo __('Actions'); ?></h3>
	<ul>
		<li><?php echo $this->Html->link(__('Edit Driver'), array('action' => 'edit', $driver['Driver']['id'])); ?> </li>
		<li><?php echo $this->Form->postLink(__('Delete Driver'), array('action' => 'delete', $driver['Driver']['id']), array(), __('Are you sure you want to delete # %s?', $driver['Driver']['id'])); ?> </li>
		<li><?php echo $this->Html->link(__('List Drivers'), array('action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Driver'), array('action' => 'add')); ?> </li>
		<li><?php echo $this->Html->link(__('List Trips'), array('controller' => 'trips', 'action' => 'index')); ?> </li>
		<li><?php echo $this->Html->link(__('New Trip'), array('controller' => 'trips', 'action' => 'add')); ?> </li>
	</ul>
</div>
<div class="related">
	<h3><?php echo __('Related Trips'); ?></h3>
	<?php if (!empty($driver['Trip'])): ?>
	<table cellpadding = "0" cellspacing = "0">
	<tr>
		<th><?php echo __('Id'); ?></th>
		<th><?php echo __('Customer Id'); ?></th>
		<th><?php echo __('Driver Id'); ?></th>
		<th><?php echo __('Start Time'); ?></th>
		<th><?php echo __('Start Lat'); ?></th>
		<th><?php echo __('Start Lng'); ?></th>
		<th><?php echo __('Stop Time'); ?></th>
		<th><?php echo __('Stop Lat'); ?></th>
		<th><?php echo __('Stop Lng'); ?></th>
		<th><?php echo __('Base Fare Per Km'); ?></th>
		<th><?php echo __('Total Distance'); ?></th>
		<th><?php echo __('Status'); ?></th>
		<th class="actions"><?php echo __('Actions'); ?></th>
	</tr>
	<?php foreach ($driver['Trip'] as $trip): ?>
		<tr>
			<td><?php echo $trip['id']; ?></td>
			<td><?php echo $trip['customer_id']; ?></td>
			<td><?php echo $trip['driver_id']; ?></td>
			<td><?php echo $trip['start_time']; ?></td>
			<td><?php echo $trip['start_lat']; ?></td>
			<td><?php echo $trip['start_lng']; ?></td>
			<td><?php echo $trip['stop_time']; ?></td>
			<td><?php echo $trip['stop_lat']; ?></td>
			<td><?php echo $trip['stop_lng']; ?></td>
			<td><?php echo $trip['base_fare_per_km']; ?></td>
			<td><?php echo $trip['total_distance']; ?></td>
			<td><?php echo $trip['status']; ?></td>
			<td class="actions">
				<?php echo $this->Html->link(__('View'), array('controller' => 'trips', 'action' => 'view', $trip['id'])); ?>
				<?php echo $this->Html->link(__('Edit'), array('controller' => 'trips', 'action' => 'edit', $trip['id'])); ?>
				<?php echo $this->Form->postLink(__('Delete'), array('controller' => 'trips', 'action' => 'delete', $trip['id']), array(), __('Are you sure you want to delete # %s?', $trip['id'])); ?>
			</td>
		</tr>
	<?php endforeach; ?>
	</table>
<?php endif; ?>

	<div class="actions">
		<ul>
			<li><?php echo $this->Html->link(__('New Trip'), array('controller' => 'trips', 'action' => 'add')); ?> </li>
		</ul>
	</div>
</div>
